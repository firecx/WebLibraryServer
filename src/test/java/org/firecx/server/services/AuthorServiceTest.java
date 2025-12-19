package org.firecx.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.interfaces.mappers.AuthorMapper;
import org.firecx.server.interfaces.repository.AuthorRepository;
import org.firecx.server.models.AuthorDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class) // Включаем интеграцию Mockito с JUnit5
public class AuthorServiceTest {

    @Mock // Мок репозитория для изоляции теста
    private AuthorRepository authorRepository;

    @Mock // Мок маппера для преобразований Entity <-> DTO
    private AuthorMapper authorMapper;

    @InjectMocks // Внедряем моки в тестируемый сервис
    private AuthorService authorService; // Тестируемый экземпляр AuthorService

    @Test // Тест для проверки метода findAll
    public void findAll_returnsMappedDTOs() {
        AuthorEntity e1 = new AuthorEntity(); // Создаём первую сущность
        e1.setId(1); // Устанавливаем id для первой сущности
        e1.setNickname("nick1"); // Устанавливаем nickname для первой сущности

        AuthorEntity e2 = new AuthorEntity(); // Создаём вторую сущность
        e2.setId(2); // Устанавливаем id для второй сущности
        e2.setNickname("nick2"); // Устанавливаем nickname для второй сущности

        AuthorDTO d1 = new AuthorDTO(); // Создаём DTO для первой сущности
        d1.setId(1); // Устанавливаем id в DTO1
        d1.setNickname("nick1"); // Устанавливаем nickname в DTO1

        AuthorDTO d2 = new AuthorDTO(); // Создаём DTO для второй сущности
        d2.setId(2); // Устанавливаем id в DTO2
        d2.setNickname("nick2"); // Устанавливаем nickname в DTO2

        when(authorRepository.findAll()).thenReturn(List.of(e1, e2)); // Репозиторий для возврата двух сущностей
        when(authorMapper.toDTO(e1)).thenReturn(d1); // Маппер для первой сущности
        when(authorMapper.toDTO(e2)).thenReturn(d2); // Маппер для второй сущности

        List<AuthorDTO> result = authorService.findAll(); // Вызываем тестируемый метод

        assertEquals(2, result.size()); // Проверяем, что возвращено два DTO
        assertEquals("nick1", result.get(0).getNickname()); // Проверяем содержимое первого DTO
        assertEquals("nick2", result.get(1).getNickname()); // Проверяем содержимое второго DTO
    } // Конец теста findAll

    @Test // Тест для проверки findById в успешном случае
    public void findById_found_returnsDTO() {
        AuthorEntity e = new AuthorEntity(); // Создаём сущность
        e.setId(10); // Устанавливаем id сущности
        e.setNickname("nick10"); // Устанавливаем nickname сущности

        AuthorDTO d = new AuthorDTO(); // Создаём соответствующий DTO
        d.setId(10); // Устанавливаем id в DTO
        d.setNickname("nick10"); // Устанавливаем nickname в DTO

        when(authorRepository.findById(10)).thenReturn(Optional.of(e)); // Репозиторий по id
        when(authorMapper.toDTO(e)).thenReturn(d); // Маппер для сущности

        AuthorDTO result = authorService.findById(10); // Вызываем метод сервиса

        assertEquals(10, result.getId()); // Проверяем id в результате
        assertEquals("nick10", result.getNickname()); // Проверяем nickname в результате
    }

    @Test // Тест для проверки создания автора
    public void createAuthor_valid_savesAndReturnsDTO() {
        AuthorDTO request = new AuthorDTO(); // Создаём DTO запроса
        request.setNickname("newNick"); // Устанавливаем nickname в запросе

        AuthorEntity toSave = new AuthorEntity(); // Создаём сущность, которой будет соответствовать DTO
        toSave.setNickname("newNick"); // Устанавливаем nickname в сущности

        AuthorEntity saved = new AuthorEntity(); // Создаём сущность, которая вернётся из save
        saved.setId(5); // Устанавливаем id у сохранённой сущности
        saved.setNickname("newNick"); // Устанавливаем nickname у сохранённой сущности

        AuthorDTO savedDto = new AuthorDTO(); // Создаём DTO, который вернёт маппер после сохранения
        savedDto.setId(5); // Устанавливаем id в итоговом DTO
        savedDto.setNickname("newNick"); // Устанавливаем nickname в итоговом DTO

        when(authorRepository.findByNickname("newNick")).thenReturn(Optional.empty()); // Проверяем, что автора с таким nickname нет
        when(authorMapper.toEntity(request)).thenReturn(toSave); // Преобразование DTO -> Entity
        when(authorRepository.save(toSave)).thenReturn(saved); // Сохранение в репозитории
        when(authorMapper.toDTO(saved)).thenReturn(savedDto); // Преобразование сохранённой Entity -> DTO

        AuthorDTO result = authorService.createAuthor(request); // Вызываем метод createAuthor

        assertEquals(5, result.getId()); // Убедимся, что вернулся DTO с ожидаемым id
        assertEquals("newNick", result.getNickname()); // Проверяем nickname в возвращённом DTO
    } // Конец теста createAuthor

    @Test // Тест для проверки update метода
    public void update_existing_updatesAndReturnsDTO() {
        AuthorDTO request = new AuthorDTO(); // Создаём DTO запроса для обновления
        request.setId(7); // Устанавливаем id в запросе
        request.setNickname("updNick"); // Устанавливаем новое значение nickname

        AuthorEntity existing = new AuthorEntity(); // Существующая сущность в репозитории
        existing.setId(7); // Устанавливаем id существующей сущности
        existing.setNickname("oldNick"); // Устанавливаем старый nickname

        AuthorEntity saved = new AuthorEntity(); // Сущность после сохранения
        saved.setId(7); // Сохраняем тот же id
        saved.setNickname("updNick"); // Устанавливаем обновлённый nickname

        AuthorDTO resultDto = new AuthorDTO(); // DTO, который вернёт маппер после сохранения
        resultDto.setId(7); // Устанавливаем id в итоговом DTO
        resultDto.setNickname("updNick"); // Устанавливаем nickname в итоговом DTO

        when(authorRepository.findById(7)).thenReturn(Optional.of(existing)); // Поиск по id
        when(authorRepository.save(existing)).thenReturn(saved); // Сохранение обновлённой сущности
        when(authorMapper.toDTO(saved)).thenReturn(resultDto); // Маппинг сохранённой сущности в DTO

        AuthorDTO result = authorService.update(request); // Вызываем update

        assertEquals(7, result.getId()); // Проверяем id результата
        assertEquals("updNick", result.getNickname()); // Проверяем, что nickname обновлён
    } 

    @Test // Тест для проверки удаления несуществующего автора вызывает исключение
    public void delete_nonExisting_throwsEntityNotFoundException() {
        when(authorRepository.existsById(99)).thenReturn(false); // existsById как false

        assertThrows(EntityNotFoundException.class, () -> authorService.delete(99)); // Ожидаем EntityNotFoundException при удалении
    }

    @Test // Тест для проверки findByNickname в случае отсутствия автора
    public void findByNickname_notFound_throwsEntityNotFound() {
        when(authorRepository.findByNickname("nope")).thenReturn(Optional.empty()); // Репозиторий возвращает пустой Optional

        assertThrows(EntityNotFoundException.class, () -> authorService.findByNickname("nope")); // Ожидаем исключение при отсутствии автора
    } 

} 

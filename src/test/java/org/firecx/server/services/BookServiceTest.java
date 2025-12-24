package org.firecx.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.entities.BookEntity;
import org.firecx.server.interfaces.mappers.AuthorMapper;
import org.firecx.server.interfaces.mappers.BookMapper;
import org.firecx.server.interfaces.repository.AuthorRepository;
import org.firecx.server.interfaces.repository.BookRepository;
import org.firecx.server.models.AuthorDTO;
import org.firecx.server.models.BookDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

// Тестовый класс для сервиса книг
@ExtendWith(MockitoExtension.class)
// Тестовый класс для сервиса книг
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    // Мок репозитория книг
    @Mock
    private BookRepository bookRepository;

    // Мок маппера для преобразования BookEntity <-> BookDTO
    @Mock
    private BookMapper bookMapper;

    // Мок маппера для преобразования AuthorEntity <-> AuthorDTO
    @Mock
    private AuthorMapper authorMapper;

    // Мок репозитория авторов
    @Mock
    private AuthorRepository authorRepository;

    // Тестируемый сервис книг с инъецированными мока
    // Тестируемый сервис книг с инъецированными мока
    @InjectMocks
    private BookService bookService;

    // Тест: получение всех книг должно вернуть маппированные DTO
    @Test
    public void findAll_returnsMappedDTOs() {
        BookEntity e1 = new BookEntity();
        e1.setId(1);
        e1.setName("Name1");

        BookEntity e2 = new BookEntity();
        e2.setId(2);
        e2.setName("Name2");

        BookDTO d1 = new BookDTO();
        d1.setId(1);
        d1.setName("Name1");

        BookDTO d2 = new BookDTO();
        d2.setId(2);
        d2.setName("Name2");

        when(bookRepository.findAll()).thenReturn(List.of(e1, e2));
        when(bookMapper.toDTO(e1)).thenReturn(d1);
        when(bookMapper.toDTO(e2)).thenReturn(d2);

        List<BookDTO> result = bookService.findAll();

        assertEquals(2, result.size());
        assertEquals("Name1", result.get(0).getName());
        assertEquals("Name2", result.get(1).getName());
    }

    // Тест: поиск существующей книги по ID должен вернуть DTO
    @Test
    public void findById_found_returnsDTO() {
        BookEntity e = new BookEntity();
        e.setId(10);
        e.setName("Book10");

        BookDTO d = new BookDTO();
        d.setId(10);
        d.setName("Book10");

        when(bookRepository.findById(10)).thenReturn(Optional.of(e));
        when(bookMapper.toDTO(e)).thenReturn(d);

        BookDTO result = bookService.findById(10);

        assertEquals(10, result.getId());
        assertEquals("Book10", result.getName());
    }

    // Тест: поиск несуществующей книги должен выбросить исключение EntityNotFoundException
    @Test
    public void findById_notFound_throwsEntityNotFoundException() {
        when(bookRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(404));
    }

    // Тест: создание валидной книги должно сохранить и вернуть DTO
    @Test
    public void createBook_valid_savesAndReturnsDTO() {
        BookDTO request = new BookDTO();
        request.setSeries("S");
        request.setName("N");
        request.setVolume(1);

        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setNickname("nick");
        request.setAuthor(authorDto);

        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(3);
        authorEntity.setNickname("nick");

        BookEntity toSave = new BookEntity();
        toSave.setSeries("S");
        toSave.setName("N");
        toSave.setVolume(1);

        BookEntity saved = new BookEntity();
        saved.setId(5);
        saved.setSeries("S");
        saved.setName("N");
        saved.setVolume(1);
        saved.setAuthor(authorEntity);

        BookDTO savedDto = new BookDTO();
        savedDto.setId(5);
        savedDto.setSeries("S");
        savedDto.setName("N");
        savedDto.setVolume(1);

        when(authorRepository.findByNickname("nick")).thenReturn(Optional.of(authorEntity));
        when(bookRepository.findBySeriesAndNameAndVolume("S", "N", 1)).thenReturn(Optional.empty());
        when(bookMapper.toEntity(request)).thenReturn(toSave);
        when(bookRepository.save(toSave)).thenReturn(saved);
        when(bookMapper.toDTO(saved)).thenReturn(savedDto);

        BookDTO result = bookService.createBook(request);

        assertEquals(5, result.getId());
        assertEquals("N", result.getName());
    }

    // Тест: создание книги с null автором должно выбросить IllegalArgumentException
    @Test
    public void createBook_nullAuthor_throwsIllegalArgumentException() {
        BookDTO request = new BookDTO();
        request.setName("N");

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(request));
    }

    // Тест: создание книги с пустым ником автора должно выбросить IllegalArgumentException
    @Test
    public void createBook_blankAuthorNickname_throwsIllegalArgumentException() {
        BookDTO request = new BookDTO();
        request.setName("N");
        request.setAuthor(new org.firecx.server.models.AuthorDTO().setNickname("")); // пустой ник

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(request));
    }

    // Тест: создание книги с неизвестным автором должно выбросить EntityNotFoundException
    @Test
    public void createBook_authorNotFound_throwsEntityNotFoundException() {
        BookDTO request = new BookDTO();
        request.setSeries("S");
        request.setName("N");
        request.setVolume(1);
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setNickname("unknown");
        request.setAuthor(authorDto);

        when(authorRepository.findByNickname("unknown")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.createBook(request));
    }

    // Тест: создание уже существующей книги должно выбросить IllegalArgumentException
    @Test
    public void createBook_existing_throwsIllegalArgumentException() {
        BookDTO request = new BookDTO();
        request.setSeries("S");
        request.setName("N");
        request.setVolume(1);
        AuthorDTO authorDto = new AuthorDTO();
        authorDto.setNickname("nick");
        request.setAuthor(authorDto);

        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setNickname("nick");

        BookEntity existing = new BookEntity();

        when(authorRepository.findByNickname("nick")).thenReturn(Optional.of(authorEntity));
        when(bookRepository.findBySeriesAndNameAndVolume("S", "N", 1)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(request));
    }

    // Тест: обновление существующей книги должно обновить и вернуть DTO
    @Test
    public void update_existing_updatesAndReturnsDTO() {
        BookDTO request = new BookDTO();
        request.setId(7);
        request.setName("updName");

        BookEntity existing = new BookEntity();
        existing.setId(7);
        existing.setName("oldName");

        BookEntity saved = new BookEntity();
        saved.setId(7);
        saved.setName("updName");

        BookDTO resultDto = new BookDTO();
        resultDto.setId(7);
        resultDto.setName("updName");

        when(bookRepository.findById(7)).thenReturn(Optional.of(existing));
        when(bookRepository.save(existing)).thenReturn(saved);
        when(bookMapper.toDTO(saved)).thenReturn(resultDto);

        BookDTO result = bookService.update(request);

        assertEquals(7, result.getId());
        assertEquals("updName", result.getName());
    }

    // Тест: обновление несуществующей книги должно выбросить EntityNotFoundException
    @Test
    public void update_notExisting_throwsEntityNotFoundException() {
        BookDTO request = new BookDTO();
        request.setId(999);
        request.setName("upd");

        when(bookRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(request));
    }

    // Тест: удаление несуществующей книги должно выбросить EntityNotFoundException
    @Test
    public void delete_nonExisting_throwsEntityNotFoundException() {
        when(bookRepository.existsById(99)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bookService.delete(99));
    }
}

package org.firecx.server.interfaces.mappers;
import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.models.AuthorDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorMapperTest {

    private final AuthorMapper mapper = Mappers.getMapper(AuthorMapper.class); // Инициализация маппера через MapStruct

    @Test
    public void testToEntity() { // Тестирование преобразования DTO -> Entity
        AuthorDTO dto = new AuthorDTO() // Создаём DTO с тестовыми значениями
                .setId(1) // Устанавливаем id
                .setNickname("nick") // Устанавливаем nickname
                .setName("Name") // Устанавливаем имя
                .setSurname("Surname"); // Устанавливаем фамилию

        AuthorEntity entity = mapper.toEntity(dto); // Вызываем метод маппера для преобразования

        assertNotNull(entity); // Проверяем, что результат не null
        assertEquals(dto.getId(), entity.getId()); // Сравниваем id
        assertEquals(dto.getNickname(), entity.getNickname()); // Сравниваем nickname
        assertEquals(dto.getName(), entity.getName()); // Сравниваем name
        assertEquals(dto.getSurname(), entity.getSurname()); // Сравниваем surname
    }

    @Test
    public void testToDTO() { // Тестирование преобразования Entity -> DTO
        AuthorEntity entity = new AuthorEntity() // Создаём Entity с тестовыми значениями
                .setId(2) // Устанавливаем id
                .setNickname("nick2") // Устанавливаем nickname
                .setName("N2") // Устанавливаем name
                .setSurname("S2"); // Устанавливаем surname

        AuthorDTO dto = mapper.toDTO(entity); // Вызываем метод маппера для обратного преобразования

        assertNotNull(dto); // Проверяем, что DTO не null
        assertEquals(entity.getId(), dto.getId()); // Сравниваем id
        assertEquals(entity.getNickname(), dto.getNickname()); // Сравниваем nickname
        assertEquals(entity.getName(), dto.getName()); // Сравниваем name
        assertEquals(entity.getSurname(), dto.getSurname()); // Сравниваем surname
    }
}
package org.firecx.server.interfaces.mappers;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.entities.BookEntity;
import org.firecx.server.models.AuthorDTO;
import org.firecx.server.models.BookDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTest {

    private final BookMapper mapper = Mappers.getMapper(BookMapper.class); // Получаем реализацию BookMapper

    @Test
    public void testToEntity() { // Тест преобразования BookDTO -> BookEntity
        AuthorDTO authorDto = new AuthorDTO() // Создаём вложенный AuthorDTO
                .setId(10) // Устанавливаем id автора
                .setNickname("aNick") // Устанавливаем nickname
                .setName("AName") // Устанавливаем имя
                .setSurname("ASurname"); // Устанавливаем фамилию

        BookDTO dto = new BookDTO() // Создаём BookDTO с полями
                .setId(5) // Устанавливаем id книги
                .setSeries("SeriesX") // Устанавливаем серию
                .setName("BookName") // Устанавливаем название
                .setVolume(1) // Устанавливаем том
                .setAuthor(authorDto); // Привязываем AuthorDTO к BookDTO

        BookEntity entity = mapper.toEntity(dto); // Выполняем маппинг DTO -> Entity

        assertNotNull(entity); // Проверяем, что entity не null
        assertEquals(dto.getId(), entity.getId()); // Сверяем id книги
        assertEquals(dto.getSeries(), entity.getSeries()); // Сверяем series
        assertEquals(dto.getName(), entity.getName()); // Сверяем name
        assertEquals(dto.getVolume(), entity.getVolume()); // Сверяем volume
    }

    @Test
    public void testToDTO() { // Тест преобразования BookEntity -> BookDTO
        AuthorEntity authorEntity = new AuthorEntity() // Создаём вложенный AuthorEntity
                .setId(11) // Устанавливаем id автора
                .setNickname("aNick2") // Устанавливаем nickname
                .setName("AName2") // Устанавливаем имя
                .setSurname("ASurname2"); // Устанавливаем фамилию

        BookEntity entity = new BookEntity() // Создаём BookEntity с полями
                .setId(6) // Устанавливаем id книги
                .setSeries("SeriesY") // Устанавливаем серию
                .setName("AnotherBook") // Устанавливаем название
                .setVolume(2) // Устанавливаем том
                .setAuthor(authorEntity); // Привязываем AuthorEntity к BookEntity

        BookDTO dto = mapper.toDTO(entity); // Выполняем маппинг Entity -> DTO

        assertNotNull(dto); // Проверяем, что dto не null
        assertEquals(entity.getId(), dto.getId()); // Сверяем id книги
        assertEquals(entity.getSeries(), dto.getSeries()); // Сверяем series
        assertEquals(entity.getName(), dto.getName()); // Сверяем name
        assertEquals(entity.getVolume(), dto.getVolume()); // Сверяем volume
    }
}
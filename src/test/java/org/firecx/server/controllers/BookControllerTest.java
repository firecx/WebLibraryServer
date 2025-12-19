package org.firecx.server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.firecx.server.models.AuthorDTO;
import org.firecx.server.models.BookDTO;
import org.firecx.server.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.mockito.Mockito;
import org.springframework.http.MediaType; 
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class) // Загружает только слой веб (контроллер) для тестирования `BookController`
@Import(BookControllerTest.TestConfig.class) // Импортируем тестовую конфигурацию, определённую ниже
public class BookControllerTest {

    @Autowired // Внедряется MockMvc — позволяет выполнять HTTP-запросы в тестах
    private MockMvc mockMvc;

    @Autowired // Внедряется ObjectMapper для сериализации/десериализации JSON
    private ObjectMapper objectMapper;

    @Autowired // Внедряется BookService — в тесте это мок из TestConfig
    private BookService bookService;

    @Test // Тестирование сценария получения списка всех книг
    void findAll_returnsListOfBooks() throws Exception {
        BookDTO book = new BookDTO() // Создаём тестовый объект BookDTO
            .setId(1) // Устанавливаем id = 1
            .setName("Example Name") // Устанавливаем имя книги
            .setSeries("Example Series") // Устанавливаем серию
            .setVolume(1) // Устанавливаем том/номер
            .setAuthor(new AuthorDTO().setNickname("nick")); // Вложенный AuthorDTO с никнеймом

        when(bookService.findAll()).thenReturn(List.of(book)); // Мокаем поведение сервиса: вернуть список с нашим объектом

        mockMvc.perform(get("/api/books").accept(MediaType.APPLICATION_JSON)) // Выполняем GET /api/books, ожидаем JSON
            .andExpect(status().isOk()) // Ожидаем 200 OK
            .andExpect(jsonPath("$[0].id").value(1)) // Проверяем, что в ответе первый элемент имеет id = 1
            .andExpect(jsonPath("$[0].name").value("Example Name")); // Проверяем имя первой книги
    }

    @Test // Тестирование эндпоинта создания книги (POST)
    void create_returnsCreatedBook() throws Exception {
        BookDTO request = new BookDTO() // DTO, который отправляем в запросе
            .setSeries("S") // Устанавливаем серию
            .setName("N") // Устанавливаем название
            .setVolume(1) // Устанавливаем том/номер
            .setAuthor(new AuthorDTO().setNickname("nick")); // Автор с никнеймом

        BookDTO created = new BookDTO() // DTO, который вернёт мок при создании
            .setId(42) // Примерный id созданной книги
            .setSeries("S") // Совпадает с request
            .setName("N") // Совпадает с request
            .setVolume(1) // Совпадает с request
            .setAuthor(new AuthorDTO().setNickname("nick")); // Совпадает с request

        when(bookService.createBook(any(BookDTO.class))).thenReturn(created); // Настраиваем мок для createBook

        mockMvc.perform(post("/api/books") // Выполняем POST-запрос к /api/books
                .contentType(MediaType.APPLICATION_JSON) // Указываем content-type JSON
                .content(objectMapper.writeValueAsString(request))) // Сериализуем request в JSON и кладём в тело
            .andExpect(status().isOk()) // Ожидаем 200 OK (реализация теста предполагает такой ответ)
            .andExpect(jsonPath("$.id").value(42)) // Проверяем, что в теле ответа id = 42
            .andExpect(jsonPath("$.name").value("N")); // Проверяем, что в теле ответа name = "N"
    }

    @TestConfiguration // Внутренний класс с конфигурацией бинов для тестового контекста
    static class TestConfig {
        @Bean // Объявляем бин BookService и возвращаем его мок-реализацию
        public BookService bookService() {
            return Mockito.mock(BookService.class); // Создаём и возвращаем мок BookService
        }
    }

}

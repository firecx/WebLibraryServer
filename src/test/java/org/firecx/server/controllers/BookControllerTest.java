package org.firecx.server.controllers; // Пакет, в котором находится этот тестовый класс (организация кода)

import static org.mockito.ArgumentMatchers.any; // Статический импорт матчера any() из Mockito
import static org.mockito.Mockito.when; // Статический импорт when() для задания поведения моков
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Билдер GET-запроса для MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // Билдер POST-запроса для MockMvc
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Проверки JSON-путей в ответе
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Проверки HTTP-статуса в ответе

import java.util.List; // Интерфейс списка из стандартной библиотеки Java

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson ObjectMapper для JSON-сериализации

import org.firecx.server.models.AuthorDTO; // DTO для автора — используется в тестовых данных
import org.firecx.server.models.BookDTO; // DTO для книги — используется в тестовых данных
import org.firecx.server.services.BookService; // Сервис для работы с книгами — будет мокироваться
import org.junit.jupiter.api.Test; // Аннотация @Test из JUnit 5
import org.springframework.beans.factory.annotation.Autowired; // Для внедрения зависимостей в тесте
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // Настройка тестового контекста MVC
import org.springframework.context.annotation.Import; // Позволяет импортировать дополнительную конфигурацию в тест
import org.springframework.boot.test.context.TestConfiguration; // Конфигурация, специфичная для тестов
import org.springframework.context.annotation.Bean; // Для объявления бина внутри TestConfiguration
import org.mockito.Mockito; // Класс Mockito для создания моков
import org.springframework.http.MediaType; // Типы содержимого (например, application/json)
import org.springframework.test.web.servlet.MockMvc; // Инструмент для имитации HTTP-запросов к контроллерам

@WebMvcTest(BookController.class) // Загружает только слой веб (контроллер) для тестирования `BookController`
@Import(BookControllerTest.TestConfig.class) // Импортируем тестовую конфигурацию, определённую ниже
public class BookControllerTest { // Тестовый класс для контроллера книг

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

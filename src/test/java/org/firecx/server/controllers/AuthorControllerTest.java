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
import org.firecx.server.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;

@WebMvcTest(AuthorController.class)
@Import(AuthorControllerTest.TestConfig.class)
// Тестовый класс для контроллера `AuthorController`.
// Содержит юнит-тесты, использующие MockMvc для вызова HTTP-эндпоинтов.
public class AuthorControllerTest {

    // MockMvc используется для имитации HTTP-запросов к контроллеру
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper используется для сериализации/десериализации JSON в тестах
    @Autowired
    private ObjectMapper objectMapper;

    // Мок-сервис авторов, поведение которого настраивается в тестах
    @Autowired
    private AuthorService authorService;

    // Очищаем состояние мока перед каждым тестом для независимости тестов
    @BeforeEach
    void setUp() {
        Mockito.reset(authorService);
    }

    // Тест: проверяет, что GET /api/authors возвращает список авторов в формате JSON
    // Подготавливает пример объекта AuthorDTO, настраивает мок-сервис и проверяет ответ
    @Test
    void findAll_returnsListOfAuthors() throws Exception {
        AuthorDTO author = new AuthorDTO()
            .setId(1)
            .setNickname("nick")
            .setName("John")
            .setSurname("Doe");

        when(authorService.findAll()).thenReturn(List.of(author));

        mockMvc.perform(get("/api/authors").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nickname").value("nick"))
            .andExpect(jsonPath("$[0].name").value("John"))
            .andExpect(jsonPath("$[0].surname").value("Doe"));
    }

    // Тест: проверяет создание автора через POST /api/authors
    // Отправляет JSON с данными автора и ожидает в ответе объект с установленным id
    @Test
    void create_returnsCreatedAuthor() throws Exception {
        AuthorDTO request = new AuthorDTO()
            .setNickname("nick")
            .setName("John")
            .setSurname("Doe");

        AuthorDTO created = new AuthorDTO()
            .setId(42)
            .setNickname("nick")
            .setName("John")
            .setSurname("Doe");

        when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(42))
            .andExpect(jsonPath("$.nickname").value("nick"))
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.surname").value("Doe"));
    }

    @Test
    // Негативный сценарий: невалидный JSON в теле запроса — ожидаем 400 Bad Request (ошибка парсинга)
    void create_withMalformedJson_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
            .andExpect(status().isBadRequest());
    }

    @TestConfiguration
    // Внутренняя тестовая конфигурация: предоставляет мок-реализацию `AuthorService`
    static class TestConfig {
        // Регистрируем бин `AuthorService`, возвращающий Mockito-мок
        @Bean
        public AuthorService authorService() {
            return Mockito.mock(AuthorService.class);
        }
    }

}

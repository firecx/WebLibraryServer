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

@WebMvcTest(AuthorController.class)
@Import(AuthorControllerTest.TestConfig.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorService authorService;

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

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AuthorService authorService() {
            return Mockito.mock(AuthorService.class);
        }
    }

}

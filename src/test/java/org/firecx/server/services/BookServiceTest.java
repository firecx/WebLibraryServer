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

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

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

    @Test
    public void createBook_nullAuthor_throwsIllegalArgumentException() {
        BookDTO request = new BookDTO();
        request.setName("N");

        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(request));
    }

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

    @Test
    public void delete_nonExisting_throwsEntityNotFoundException() {
        when(bookRepository.existsById(99)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bookService.delete(99));
    }
}

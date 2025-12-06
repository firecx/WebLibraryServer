package org.firecx.server.services;

import java.util.List;
import java.util.Optional;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.entities.BookEntity;
import org.firecx.server.interfaces.mappers.AuthorMapper;
import org.firecx.server.interfaces.mappers.BookMapper;
import org.firecx.server.interfaces.repository.AuthorRepository;
import org.firecx.server.interfaces.repository.BookRepository;
import org.firecx.server.interfaces.services.IBookService;
import org.firecx.server.models.BookDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService{
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        return bookRepository.findAll()
        .stream()
        .map(bookMapper::toDTO)
        .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO findById(@NonNull Integer bookId) {
        return bookRepository.findById(bookId)
        .map(bookMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Book "+ bookId + " is not found"));
    }

    @Override
    @Transactional
    public BookDTO createBook(@NonNull BookDTO bookDto) {
        if (bookDto.getAuthor() == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }

        String nickname = bookDto.getAuthor().getNickname();
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Author nickname cannot be null or blank");
        }

        AuthorEntity author = authorRepository.findByNickname(nickname)
        .orElseThrow(() -> new EntityNotFoundException("Author " + nickname + " is not found"));
        
        // Check if the book already exists (by series + name + volume)
        if (bookRepository.findBySeriesAndNameAndVolume(
            bookDto.getSeries(), bookDto.getName(), bookDto.getVolume()).isPresent()) {
            throw new IllegalArgumentException("Book already exists");
        }

        BookEntity bookEntity = bookMapper.toEntity(bookDto);
        bookEntity.setAuthor(author);
        BookEntity bookSaved = bookRepository.save(bookEntity);
        
        return bookMapper.toDTO(bookSaved);
    }

    @Override
    @Transactional
    public BookDTO update(@NonNull BookDTO request) {
        BookEntity bookEntity = bookRepository.findById(request.getId())
        .orElseThrow(() -> new EntityNotFoundException("Book " + request.getId() + " is not found"));
        bookUpdate(bookEntity, request);
        
        BookEntity updatedBook = bookRepository.save(bookEntity);

        return bookMapper.toDTO(updatedBook);
    }

    @Override
    @Transactional
    public void delete(@NonNull Integer bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book " + bookId + " is not sound");
        }
        bookRepository.deleteById(bookId);
    }
    
    private void bookUpdate(@NonNull BookEntity book, @NonNull BookDTO request) {
        Optional.ofNullable(request.getSeries()).ifPresent(book::setSeries);
        Optional.ofNullable(request.getName()).ifPresent(book::setName);
        Optional.ofNullable(request.getVolume()).ifPresent(book::setVolume);
        
        if (request.getAuthor() != null) {
            AuthorEntity authorEntity = authorMapper.toEntity(request.getAuthor());
            book.setAuthor(authorEntity);
        }
    }
}

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

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        return bookRepository.findAll()
        .stream()
        .map(bookMapper::toDTO)
        .toList();
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public BookDTO findById(@NonNull Integer bookId) {
        return bookRepository.findById(bookId)
        .map(bookMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Book "+ bookId + " is not found"));
    }

    @NonNull
    @Override
    @Transactional
    public BookDTO createBook(@NonNull BookDTO bookDto) {
        if (bookDto.getAuthor().getNickname() == null) {
            throw new IllegalArgumentException("Author nickname cannot be null");
        }

        AuthorEntity author = authorRepository.findByNickname(bookDto.getAuthor().getNickname())
        .orElseThrow(() -> new EntityNotFoundException("Author " + bookDto.getAuthor().getNickname() +" is not found"));
        
        BookEntity bookEntity = bookMapper.toEntity(bookDto);
        bookEntity.setAuthor(author);
        BookEntity bookSaved = bookRepository.save(bookEntity);
        
        return bookMapper.toDTO(bookSaved);
    }

    @NonNull
    @Override
    @Transactional
    public BookDTO update(@NonNull Integer bookId, @NonNull BookDTO request) {
        BookEntity bookEntity = bookRepository.findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " is not found"));
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

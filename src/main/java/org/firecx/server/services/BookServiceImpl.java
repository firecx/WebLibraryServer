package org.firecx.server.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.firecx.server.entities.Author;
import org.firecx.server.entities.Book;
import org.firecx.server.interfaces.repository.BookRepository;
import org.firecx.server.interfaces.services.BookService;
import org.firecx.server.models.AuthorResponse;
import org.firecx.server.models.BookResponse;
import org.firecx.server.models.CreateBookRequest;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
    private static BookRepository bookRepository;

    @NonNull
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll()
        .stream()
        .map(this::buildBookResponse)
        .collect(Collectors.toList());
    }

    @NonNull
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public BookResponse findById(@NonNull Integer bookId) {
        return bookRepository.findById(bookId)
        .map(this::buildBookResponse)
        .orElseThrow(() -> new EntityNotFoundException("Book "+ bookId + " is not found"));
    }

    @NonNull
    @Override
    @org.springframework.transaction.annotation.Transactional
    public BookResponse createBook(@NonNull CreateBookRequest request) {
        Book book = buildBookRequest(request);
        return buildBookResponse(bookRepository.save(book));
    }

    @NonNull
    @Override
    @org.springframework.transaction.annotation.Transactional
    public BookResponse update(@NonNull Integer bookId, @NonNull CreateBookRequest request) {
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new EntityNotFoundException("Book " + bookId + " is not found"));
        bookUpdate(book, request);
        return buildBookResponse(bookRepository.save(book));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void delete(@NonNull Integer bookId) {
        bookRepository.deleteById(bookId);
    }

    @NonNull
    private BookResponse buildBookResponse(@NonNull Book book) {
        return new BookResponse()
        .setId(book.getId())
        .setSeries(book.getSeries())
        .setName(book.getName())
        .setVolume(book.getVolume())
        .setAuthor(new AuthorResponse()
        .setId(book.getAuthor().getId())
        .setSurmane(book.getAuthor().getSurmane())
        .setName(book.getAuthor().getName())
        .setNickname(book.getAuthor().getNickname()));
    }

    @NonNull
    private Book buildBookRequest(@NonNull CreateBookRequest request) {
        return new Book()
        .setSeries(request.getSeries())
        .setName(request.getName())
        .setVolume(request.getVolume())
        .setAuthor(new Author()
        .setId(request.getAuthor().getId())
        .setSurmane(request.getAuthor().getSurmane())
        .setName(request.getAuthor().getName())
        .setNickname(request.getAuthor().getNickname()));
    }
    
    private void bookUpdate(@NonNull Book book, @NonNull CreateBookRequest request) {
        Optional.ofNullable(request.getSeries()).map(book::setSeries);
        Optional.ofNullable(request.getName()).map(book::setName);
        Optional.ofNullable(request.getVolume()).map(book::setVolume);
    }
}

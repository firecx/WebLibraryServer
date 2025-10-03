package org.firecx.server.controllers;

import java.util.List;

import org.firecx.server.models.BookResponse;
import org.firecx.server.models.CreateBookRequest;
import org.firecx.server.services.BookServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl bookService;

    @GetMapping(produces = "application/json")
    public List<BookResponse> findAll() {
        return bookService.findAll();
    }

    @GetMapping(value = "/{bookId}", produces = "application/json")
    public BookResponse findById(@PathVariable Integer bookId) {
        return bookService.findById(bookId);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public BookResponse create(@RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    @PatchMapping(value = "/{bookId}", consumes = "application/json", produces = "application/json")
    public BookResponse update(@PathVariable Integer bookId, @RequestBody CreateBookRequest request) {
        return bookService.update(bookId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{bookId}", produces = "application/json")
    public void delete(@PathVariable Integer bookId) {
        bookService.delete(bookId);
    }
}

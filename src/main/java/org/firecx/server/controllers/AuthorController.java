package org.firecx.server.controllers;

import java.util.List;

import org.firecx.server.models.AuthorDTO;
import org.firecx.server.services.AuthorService;
import org.springframework.http.ResponseEntity;

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
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping(produces = "application/json")
    public List<AuthorDTO> findAll() {
        return authorService.findAll();
    }

    @GetMapping(value = "/{authorId}", produces = "application/json")
    public AuthorDTO findById(@PathVariable Integer authorId) {
        return authorService.findById(authorId);
    }
    
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthorDTO> create(@RequestBody AuthorDTO request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @PatchMapping(value = "/{authorId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthorDTO> update(@RequestBody AuthorDTO request) {
        return ResponseEntity.ok(authorService.update(request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{authorId}", produces = "application/json")
    public void delete(@PathVariable Integer authorId) {
        authorService.delete(authorId);
    }
}

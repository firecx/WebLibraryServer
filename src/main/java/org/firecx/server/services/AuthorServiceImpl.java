package org.firecx.server.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.firecx.server.entities.Author;
import org.firecx.server.interfaces.repository.AuthorRepository;
import org.firecx.server.interfaces.services.AuthorService;
import org.firecx.server.models.AuthorResponse;
import org.firecx.server.models.CreateAuthorRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository authorRepository;

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> findAll() {
        return authorRepository.findAll()
        .stream()
        .map(this::buildAuthorResponse)
        .collect(Collectors.toList());
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public AuthorResponse findById(@NonNull Integer authorId) {
        return authorRepository.findById(authorId)
        .map(this::buildAuthorResponse)
        .orElseThrow(() -> new EntityNotFoundException("Author " + authorId + " is not sound"));
    }

    @NonNull
    @Override
    @Transactional
    public AuthorResponse createAuthor(@NonNull CreateAuthorRequest request) {
        Author author = buildAuthorRequest(request);
        return buildAuthorResponse(authorRepository.save(author));
    }

    @NonNull
    @Override
    @Transactional
    public AuthorResponse update(@NonNull Integer authorId, @NonNull CreateAuthorRequest request) {
        Author author = authorRepository.findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("Author " + authorId + " is not sound"));
        authorUpdate(author, request);
        return buildAuthorResponse(authorRepository.save(author));
    }

    @Override
    @Transactional
    public void delete(@NonNull Integer authorId) {
        authorRepository.deleteById(authorId);
    }

    @NonNull
    private AuthorResponse buildAuthorResponse(@NonNull Author author) {
        return new AuthorResponse()
        .setId(author.getId())
        .setSurname(author.getSurname())
        .setName(author.getName())
        .setNickname(author.getNickname());
    }

    @NonNull
    private Author buildAuthorRequest(@NonNull CreateAuthorRequest request) {
        return new Author()
        .setSurname(request.getSurname())
        .setName(request.getName())
        .setNickname(request.getNickname());
    }

    private void authorUpdate(@NonNull Author author, @NonNull CreateAuthorRequest request) {
        Optional.ofNullable(request.getNickname()).map(author::setNickname);
    }
}

package org.firecx.server.interfaces.services;

import java.util.List;

import org.firecx.server.entities.Author;
import org.firecx.server.models.AuthorResponse;
import org.firecx.server.models.CreateAuthorRequest;

import lombok.NonNull;

public interface AuthorService {
    @NonNull
    List<AuthorResponse> findAll();

    @NonNull
    AuthorResponse findById(@NonNull Integer authorId);

    @NonNull
    AuthorResponse createAuthor(@NonNull CreateAuthorRequest request);

    @NonNull
    AuthorResponse update(@NonNull Integer authorId, @NonNull CreateAuthorRequest request);

    void delete(@NonNull Integer authorId);

    @NonNull
    Author getAuthorReference(Integer authorId);
}

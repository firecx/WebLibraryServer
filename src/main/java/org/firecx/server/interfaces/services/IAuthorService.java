package org.firecx.server.interfaces.services;

import java.util.List;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.models.AuthorDTO;

import lombok.NonNull;

public interface IAuthorService {
    @NonNull
    List<AuthorDTO> findAll();

    @NonNull
    AuthorDTO findById(@NonNull Integer authorId);

    @NonNull
    public AuthorDTO findByNickname(@NonNull String nickname);

    @NonNull
    AuthorDTO createAuthor(@NonNull AuthorDTO request);

    @NonNull
    AuthorDTO update(@NonNull Integer authorId, @NonNull AuthorDTO request);

    void delete(@NonNull Integer authorId);

    @NonNull
    AuthorEntity getAuthorReference(Integer authorId);
}

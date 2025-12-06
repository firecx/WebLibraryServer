package org.firecx.server.interfaces.services;

import java.util.List;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.models.AuthorDTO;

import lombok.NonNull;

public interface IAuthorService {
    List<AuthorDTO> findAll();

    AuthorDTO findById(@NonNull Integer authorId);

    AuthorDTO findByNickname(@NonNull String nickname);

    AuthorDTO createAuthor(@NonNull AuthorDTO request);

    AuthorDTO update(@NonNull AuthorDTO request);

    void delete(@NonNull Integer authorId);

    AuthorEntity getAuthorReference(Integer authorId);
}

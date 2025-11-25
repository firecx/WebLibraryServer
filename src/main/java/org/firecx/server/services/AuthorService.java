package org.firecx.server.services;

import java.util.List;
import java.util.Optional;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.interfaces.mappers.AuthorMapper;
import org.firecx.server.interfaces.repository.AuthorRepository;
import org.firecx.server.interfaces.services.IAuthorService;
import org.firecx.server.models.AuthorDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService implements IAuthorService{
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        return authorRepository.findAll()
        .stream()
        .map(authorMapper::toDTO)
        .toList();
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public AuthorDTO findById(@NonNull Integer authorId) {
        return authorRepository.findById(authorId)
        .map(authorMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Author " + authorId + " is not found"));
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public AuthorDTO findByNickname(@NonNull String nickname) {
        return authorRepository.findByNickname(nickname)
        .map(authorMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Author " + nickname + " is not found"));
    }

    @NonNull
    @Override
    @Transactional
    public AuthorDTO createAuthor(@NonNull AuthorDTO authorDto) {
        AuthorEntity authorEntity = authorMapper.toEntity(authorDto);
        AuthorEntity authorSaved = authorRepository.save(authorEntity);
        return authorMapper.toDTO(authorSaved);
    }

    @NonNull
    @Override
    @Transactional
    public AuthorDTO update(@NonNull Integer authorId, @NonNull AuthorDTO request) {
        AuthorEntity authorEntity = authorRepository.findById(authorId)
        .orElseThrow(() -> new EntityNotFoundException("Author " + authorId + " is not sound"));
        
        authorUpdate(authorEntity, request);

        AuthorEntity updatedAuthor = authorRepository.save(authorEntity);

        return authorMapper.toDTO(updatedAuthor);
    }

    @Override
    @Transactional
    public void delete(@NonNull Integer authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author " + authorId + " is not sound");
        }
        authorRepository.deleteById(authorId);
    }

    @NonNull
    @Override
    @Transactional(readOnly = true)
    public AuthorEntity getAuthorReference(Integer authorId) {
        return authorRepository.getReferenceById(authorId);
    }

    private void authorUpdate(@NonNull AuthorEntity author, @NonNull AuthorDTO request) {
        Optional.ofNullable(request.getNickname()).ifPresent(author::setNickname);
        Optional.ofNullable(request.getName()).ifPresent(author::setName);
        Optional.ofNullable(request.getSurname()).ifPresent(author::setSurname);
    }
}

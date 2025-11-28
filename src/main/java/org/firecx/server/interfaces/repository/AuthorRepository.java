package org.firecx.server.interfaces.repository;

import java.util.Optional;

import org.firecx.server.entities.AuthorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
    Optional<AuthorEntity> findByNickname(String nickname);
}

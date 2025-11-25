package org.firecx.server.interfaces.repository;

import java.util.Optional;

import org.firecx.server.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    @Query("SELECT a FROM AuthorEntity a WHERE a.nickname = :nickname")
    Optional<AuthorEntity> findByNickname(@Param("nickname") String nickname);

}

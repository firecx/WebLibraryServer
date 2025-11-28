package org.firecx.server.interfaces.repository;

import java.util.Optional;

import org.firecx.server.entities.BookEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
	Optional<BookEntity> findBySeriesAndNameAndVolume(String series, String name, Integer volume);
}

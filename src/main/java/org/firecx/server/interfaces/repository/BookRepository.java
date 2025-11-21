package org.firecx.server.interfaces.repository;

import org.firecx.server.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {}

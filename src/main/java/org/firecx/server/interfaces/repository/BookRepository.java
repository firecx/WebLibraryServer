package org.firecx.server.interfaces.repository;

import org.firecx.server.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {}

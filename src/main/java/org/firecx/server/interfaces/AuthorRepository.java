package org.firecx.server.interfaces;

import org.firecx.server.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {}

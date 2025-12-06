package org.firecx.server.interfaces.services;

import java.util.List;

import org.firecx.server.models.BookDTO;

import lombok.NonNull;

public interface IBookService {
    
    List<BookDTO> findAll();

    BookDTO findById(@NonNull Integer bookId);

    BookDTO createBook(@NonNull BookDTO request);

    BookDTO update(@NonNull BookDTO request);

    void delete(@NonNull Integer bookId);
}

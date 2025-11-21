package org.firecx.server.interfaces.services;

import java.util.List;

import org.firecx.server.models.BookDTO;

import lombok.NonNull;

public interface BookService {
    
    @NonNull
    List<BookDTO> findAll();

    @NonNull
    BookDTO findById(@NonNull Integer bookId);

    @NonNull
    BookDTO createBook(@NonNull BookDTO request);

    @NonNull
    BookDTO update(@NonNull Integer bookId, @NonNull BookDTO request);

    void delete(@NonNull Integer bookId);
}

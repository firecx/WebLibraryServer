package org.firecx.server.interfaces.services;

import java.util.List;

import org.firecx.server.models.BookResponse;
import org.firecx.server.models.CreateBookRequest;

import lombok.NonNull;

public interface BookService {
    
    @NonNull
    List<BookResponse> findAll();

    @NonNull
    BookResponse findById(@NonNull Integer bookId);

    @NonNull
    BookResponse createBook(@NonNull CreateBookRequest request);

    @NonNull
    BookResponse update(@NonNull Integer bookId, @NonNull CreateBookRequest request);

    void delete(@NonNull Integer bookId);
}

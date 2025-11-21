package org.firecx.server.interfaces.mappers;

import org.firecx.server.entities.BookEntity;
import org.firecx.server.models.BookDTO;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    
    BookEntity toEntity (BookDTO dto);

    BookDTO toDTO (BookEntity entity);
}

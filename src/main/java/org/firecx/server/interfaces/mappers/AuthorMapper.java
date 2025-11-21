package org.firecx.server.interfaces.mappers;

import org.firecx.server.entities.AuthorEntity;
import org.firecx.server.models.AuthorDTO;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    
    AuthorEntity toEntity(AuthorDTO dto);

    AuthorDTO toDTO(AuthorEntity entity);
}

package org.firecx.server.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateBookRequest {
    private Integer id;
    private String series;
    private String name;
    private Integer volume;
    private CreateAuthorRequest author;
}

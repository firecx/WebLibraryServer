package org.firecx.server.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookDTO {
    private Integer id;
    private String series;
    private String name;
    private Integer volume;
    private AuthorDTO author;
}

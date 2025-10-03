package org.firecx.server.models;

import org.firecx.server.entities.Author;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthorResponse {
    private Integer id;
    private String nickname;
    private String name;
    private String surname;

    public Author toAuthor() {
        return new Author()
        .setId(id)
        .setNickname(nickname)
        .setSurname(surname)
        .setName(name);
    }
}

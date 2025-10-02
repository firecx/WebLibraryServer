package org.firecx.server.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateAuthorRequest {
    private Integer id;
    private String nickname;
    private String name;
    private String surmane;
}

package restapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class Post {
    @Getter
    @JsonProperty
    private int id;
    @JsonProperty
    private int userId;
    @JsonProperty
    private String title;
    @Getter
    @JsonProperty
    private String body;
}



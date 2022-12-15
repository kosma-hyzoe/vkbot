package vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class Credentials {
    @Getter
    @JsonProperty
    private int userId;
    @Getter
    @JsonProperty
    private String phone;
    @Getter
    @JsonProperty
    private String password;
    @Getter
    @JsonProperty
    private String token;
}

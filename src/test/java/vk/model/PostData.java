package vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class PostData {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    @JsonProperty
    private int ownerId;
    @Getter
    @JsonProperty
    private String message;
    @Getter
    @JsonProperty
    private Attachment attachment;
}

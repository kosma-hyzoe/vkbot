package vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class Content {
    @Getter
    @Setter
    private int itemId;
    @Getter
    @JsonProperty
    private String message;
    @Getter
    @JsonProperty
    private Attachment attachment;

    public class Attachment {
    @Getter
    @JsonProperty
    private String type;
    @Getter
    @JsonProperty
    private int ownerId;
    @Getter
    @JsonProperty
    private int mediaId;

     public String getAsParameter(){
        return String.format("%s%d_%d", getType(), getOwnerId(), getMediaId());
    }
}

}

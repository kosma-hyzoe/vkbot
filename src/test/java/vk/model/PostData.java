package vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class PostData {
    @Setter
    @Getter
    private Long id;
    @Getter
    @JsonProperty
    private String author;
    @Getter
    @JsonProperty
    private String message;
    @Getter
    @JsonProperty
    private Attachment attachment;

    public String getAttachmentParameter(){
        return String.format("%s%d_%d", attachment.getType(), attachment.getOwnerId(), attachment.getMediaId());
    }

    public static class Attachment{
        @Getter
        @JsonProperty
        private String type;
        @Getter
        @JsonProperty
        private Long ownerId;
        @Getter
        @JsonProperty
        private Long mediaId;
    }
}

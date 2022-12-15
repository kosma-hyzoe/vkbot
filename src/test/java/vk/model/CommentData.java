package vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.xml.stream.events.Comment;

public class CommentData {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int ownerId;
    @Getter
    @Setter
    private int targetPostId;
    @Getter
    @Setter
    private int authorId;
    @Getter
    @JsonProperty
    private String message;
    @Getter
    @JsonProperty
    private Attachment attachment;

    public CommentData(int targetPostId, int authorId, String message){
        this.targetPostId =  targetPostId;
        this.authorId = authorId;
        this.message = message;
    }

}

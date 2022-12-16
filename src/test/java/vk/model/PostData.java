package vk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vk.util.VkRequests;
import vk.util.VkResponse;

import java.util.ArrayList;

public class PostData {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    @JsonProperty
    private int ownerId;
    @Getter
    @Setter
    @JsonProperty
    private int authorId;
    @Getter
    @JsonProperty
    private String message;
    @Getter
    @JsonProperty
    private Attachment attachment;

    public PostData getEditedCopy(PostData postEditionData){
        this.message = postEditionData.getMessage();
        this.attachment = postEditionData.getAttachment();
        return this;
    };

    public boolean isUserIdInPostLikes(int userId, String token){
        VkResponse getListOfLikesResponse = VkRequests.getLikesList("post", id, getOwnerId(), token);
        ArrayList<Integer> listOfLikersIds = getListOfLikesResponse.getItemIdList();
        for (int likerId : listOfLikersIds){
            if (likerId == userId){
                return true;
            }
        }
        return false;
    }
}

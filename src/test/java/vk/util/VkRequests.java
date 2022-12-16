package vk.util;

import kong.unirest.HttpResponse;
import vk.model.CommentData;
import vk.model.PostData;

import static vk.VkTest.getTestData;
import static vk.util.Serialization.getApiRequest;

public class VkRequests {
    private VkRequests(){}

    private static String getVkRequest(String testDataKey){
        String request = getApiRequest(testDataKey);
        String versionParameter = getTestData().get("requestsConfigParams").get("vkRequestsVersion").asText();
        return request + "&v=" + versionParameter;
    }

    public static VkResponse postOnWall(PostData postData, String token){
        String request = getVkRequest("postOnWall");
        String formattedRequest = String.format(request, postData.getOwnerId(), token);
        if (postData.getMessage() != null){
            formattedRequest += "&message=" + postData.getMessage();
        }
        if (postData.getAttachment() != null){
            formattedRequest += "&attachments=" + postData.getAttachment().getAsParameter();
        }
        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse postCommentOnWall(CommentData commentData, String token){
        String request = getVkRequest("commentPostOnWall");
        String formattedRequest = String.format(request, commentData.getTargetPostId(), commentData.getOwnerId(), token);
        if (commentData.getMessage() != null){
            formattedRequest += "&message=" + commentData.getMessage();
        }
        if (commentData.getAttachment() != null){
            formattedRequest += "&attachments=" + commentData.getAttachment().getAsParameter();
        }
        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse editPost(PostData updatedPostData, String token){
        String request = getVkRequest("editPost");
        String formattedRequest = String.format(request, updatedPostData.getId(), updatedPostData.getOwnerId(), token);

        if (updatedPostData.getMessage() != null){
            formattedRequest += "&message=" + updatedPostData.getMessage();
        }
        if (updatedPostData.getAttachment() != null){
            formattedRequest += "&attachments=" + updatedPostData.getAttachment().getAsParameter();
        }
        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse getLikesList(String type, int itemId, int ownerId, String token){
        String request = getVkRequest("getLikesList");
        String formattedRequest = String.format(request, type, itemId, ownerId, token);

        HttpResponse<String> unirestResponse = RestApiRequests.get(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse deletePost(int ownerId, int postId, String token){
        String request = getVkRequest("deletePost");
        String formattedRequest = String.format(request, postId, ownerId, token);

        HttpResponse<String> unirestResponse = RestApiRequests.get(formattedRequest);
        return new VkResponse(unirestResponse);
    }
}

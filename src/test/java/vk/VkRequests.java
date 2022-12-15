package vk;

import kong.unirest.HttpResponse;
import vk.model.CommentData;
import vk.model.Credentials;
import vk.model.PostData;
import vk.model.Response;
import vk.util.RestApiRequests;

import static vk.VkTest.getTestData;
import static vk.util.Serialization.getApiRequest;

public class VkRequests {
    private VkRequests(){}

    private static String getVkRequest(String testDataKey){
        String request = getApiRequest(testDataKey);
        String versionParameter = getTestData().get("requestsConfigParams").get("vkRequestsVersion").asText();
        return request + "&v=" + versionParameter;
    }

    public static String postOnWall(int ownerId, PostData postData, String token){
        String request = getVkRequest("postOnWall");
        String formattedRequest = String.format(request, ownerId, token);
        if (postData.getMessage() != null){
            formattedRequest += "&message=" + postData.getMessage();
        }
        if (postData.getAttachment() != null){
            formattedRequest += "&attachments=" + postData.getAttachment().getAsParameter();
        }
        return RestApiRequests.post(formattedRequest);
    }

    public static String postCommentOnWall(int ownerId, CommentData commentData, String token){
        String request = getVkRequest("commentPostOnWall");
        String formattedRequest = String.format(request, commentData.getTargetPostId(), ownerId, token);
        if (commentData.getMessage() != null){
            formattedRequest += "&message=" + commentData.getMessage();
        }
        if (commentData.getAttachment() != null){
            formattedRequest += "&attachments=" + commentData.getAttachment().getAsParameter();
        }
        return RestApiRequests.post(formattedRequest);
    }

    public static String editPost(int ownerId, PostData postData, String token){
        String request = getVkRequest("editPost");
        String formattedRequest = String.format(request, postData.getId(), ownerId, token);

        if (postData.getMessage() != null){
            formattedRequest += "&message=" + postData.getMessage();
        }
        if (postData.getAttachment() != null){
            formattedRequest += "&attachments=" + postData.getAttachment().getAsParameter();
        }
        return RestApiRequests.post(formattedRequest);
    }

    public static String likePost(){
        return null;
    }
}

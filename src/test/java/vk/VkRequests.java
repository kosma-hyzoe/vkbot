package vk;

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

    public static <T> Response<T> postOnWall(PostData postData, Credentials credentials){
        String request = getVkRequest("postOnWall");
        String formattedRequest = String.format(request, credentials.getUserId(), credentials.getToken());
        if (postData.getMessage() != null){
            formattedRequest += "&message=" + postData.getMessage();
        }
        if (postData.getAttachment() != null){
            formattedRequest += "&attachments=" + postData.getAttachmentParameter();
        }
        return RestApiRequests.post(formattedRequest);
    }

    public static <T> Response<T> commentPostOnWall(Long postId, String message, Credentials credentials){
        String request = getVkRequest("postMessageOnWall");
        String formattedRequest = String.format(request, postId, message, credentials.getUserId(), credentials.getToken());
        return RestApiRequests.post(formattedRequest);
    }

    public static <T> Response<T> editPost(PostData postData, Credentials credentials){
        String request = getVkRequest("editPost");
        String formattedRequest = String.format(request, postData.getId(), credentials.getUserId(), credentials.getToken());

        if (postData.getMessage() != null){
            formattedRequest += "&message=" + postData.getMessage();
        }
        if (postData.getAttachment() != null){
            formattedRequest += "&attachments=" + postData.getAttachmentParameter();
        }
        return RestApiRequests.post(formattedRequest);
    }
}

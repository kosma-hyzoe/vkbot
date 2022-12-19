package vk.util;

import kong.unirest.HttpResponse;
import vk.model.Content;
import vk.model.VkResponse;

import static vk.VkTest.getTestData;

public class VkRequests {
    private VkRequests() {
    }

    private static String getRequest(String methodName, Content content){
        StringBuilder requestBuilder = new StringBuilder();

        requestBuilder.append(getTestData().get("vkRequests").get("urlBase").asText());
        requestBuilder.append(getTestData().get("vkRequests").get("methods").get(methodName).asText());

        if (content != null){
            if (content.getMessage() != null) {
                requestBuilder.append("&message=").append(content.getMessage());
            }
            if (content.getAttachment() != null) {
                requestBuilder.append("&attachments=").append(content.getAttachment().getAsParameter());
            }
        }

        String ownerId = getTestData().get("vkRequests").get("sharedParams").get("ownerId").asText();
        requestBuilder.append("&ownerId=").append(ownerId);

        String versionParameter = getTestData().get("vkRequests").get("sharedParams").get("version").asText();
        requestBuilder.append("&v=").append(versionParameter);

        String accessTokenParameter = getTestData().get("vkRequests").get("sharedParams").get("accessToken").asText();
        requestBuilder.append("&access_token=").append(accessTokenParameter);

        return requestBuilder.toString();
    }

    public static VkResponse wallPost(Content content) {
        String request = getRequest("wallPost", content);

        HttpResponse<String> unirestResponse = RestApiRequests.post(request);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse wallEditPost(int postId, Content content) {
        String request = getRequest("wallEditPost", content);
        String formattedRequest = String.format(request, postId);

        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse wallCreateComment(int postId, Content content) {
        String request = getRequest("wallCreateComment", content);
        String formattedRequest = String.format(request, postId);

        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse wallDelete(int postId) {
        String request = getRequest("wallDelete", null);
        String formattedRequest = String.format(request, postId);

        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }

    public static VkResponse likesIsLiked(int userId, int itemId, String type) {
        String request = getRequest("likesIsLiked", null);
        String formattedRequest = String.format(request, userId, type, itemId);

        HttpResponse<String> unirestResponse = RestApiRequests.post(formattedRequest);
        return new VkResponse(unirestResponse);
    }
}

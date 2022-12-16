package vk.util;

import com.fasterxml.jackson.databind.JsonNode;
import kong.unirest.HttpResponse;
import lombok.Getter;

import java.util.ArrayList;

import static vk.util.Serialization.getJsonNode;


public class VkResponse {
    @Getter
    private final String body;
    @Getter
    private final int responseCode;

    public VkResponse(HttpResponse<String> httpResponse) {
        this.body = httpResponse.getBody();
        this.responseCode = httpResponse.getStatus();
    }

    public int getItemId(String itemType) {
        JsonNode responseContent = getJsonNode(getBody()).get("response");
        return responseContent.get(itemType + "_id").asInt();
    }

    public ArrayList<Integer> getItemIdsList() {
        ArrayList<Integer> itemIds = new ArrayList<>();
        JsonNode nodeItems = getJsonNode(getBody()).get("response").get("items");
        for (int i = 0; i < nodeItems.size(); i++) {
            int id = nodeItems.get(i).asInt();
            itemIds.add(id);
        }
        return itemIds;
    }

    public JsonNode getError() {
        return getJsonNode(getBody()).get("error");
    }
}

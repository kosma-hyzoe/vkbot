package vk.util;

import aquality.selenium.browser.AqualityServices;
import com.fasterxml.jackson.databind.JsonNode;
import kong.unirest.HttpResponse;
import lombok.Getter;


import static vk.util.Serialization.getJsonNode;


public class VkResponse {
    @Getter
    private final int responseCode;
    private final String body;

    public int getBody(String itemType){
        return getJsonNode(body).get("response").get(itemType + "_id").asInt();
    }

    public boolean getBody(){
        return getJsonNode(body).get("response").get("liked").asInt() == 1;
    }

    public VkResponse(HttpResponse<String> httpResponse) {
        JsonNode body = getJsonNode(httpResponse.getBody());

        JsonNode errorBody = body.findValue("error");
        if (errorBody == null) {
            this.body = httpResponse.getBody();
        }
        else {
            int errorCode = body.get("error").get("error_code").asInt();
            String errorMessage = body.get("error").get("error_msg").asText();
            AqualityServices.getLogger().error("request error " + errorCode + " - " + errorMessage);
            this.body = body.get("error").toString();
        }

        this.responseCode = httpResponse.getStatus();
    }
}

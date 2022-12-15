package vk.model;

import kong.unirest.HttpResponse;
import lombok.Getter;

import java.util.ArrayList;

import static vk.util.Serialization.deserialize;
import static vk.util.Serialization.deserializeList;

public class Response {
    private final String body;
    @Getter
    private final int responseCode;

    public String getBodyAsString(){
        return this.body;
    }

    public Response(HttpResponse<String> httpResponse) {
        this.body = httpResponse.getBody();
        this.responseCode = httpResponse.getStatus();
    }
}

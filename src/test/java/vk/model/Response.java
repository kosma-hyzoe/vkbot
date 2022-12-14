package vk.model;

import kong.unirest.HttpResponse;
import lombok.Getter;

import java.util.ArrayList;

import static vk.util.Serialization.deserialize;
import static vk.util.Serialization.deserializeList;

public class Response<T> {
    private final String body;
    @Getter
    private final int responseCode;

    public String getBodyAsString(){
        return this.body;
    }

    public ArrayList<T> getBodyAsListOfObjects(Class<T> T) {
        return deserializeList(this.body, T);
    }

    public T getBodyAsObject(Class<T> T)  {
        return deserialize(this.body, T);
    }

    public Response(HttpResponse<String> httpResponse) {
        this.body = httpResponse.getBody();
        this.responseCode = httpResponse.getStatus();
    }
}

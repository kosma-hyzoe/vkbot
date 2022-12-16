package vk.util;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Map;

public class RestApiRequests {
    private RestApiRequests() {
    }

    public static HttpResponse<String> get(String url) {
        return Unirest.get(url).asString();
    }

    public static HttpResponse<String> post(String url) {
         return Unirest.post(url).asString();
    }

    public static void shutDownUnirest() {
        Unirest.shutDown();
    }


}

package vk.util;

import vk.model.Response;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Map;

public class RestApiRequests {
    private RestApiRequests(){}

    public static String post(String url)  {
        HttpResponse<String> response = Unirest.post(url).asString();
        return response.getBody();
    }

    public static void shutDownUnirest(){
        Unirest.shutDown();
    }
}

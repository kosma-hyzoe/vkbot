package restapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import restapi.models.Response;

import java.util.Map;

public class RestApiRequests {
    private RestApiRequests(){}

    public static <T> Response<T> get(String url)  {
        HttpResponse<String> unirestResponse = Unirest.get(url).asString();
        return new Response<>(unirestResponse);
    }

    public static <T> Response<T> post(String url, String body, Map<String, String> headers)  {
        HttpResponse<String> unirestResponse = Unirest.post(url).headers(headers).body(body).asString();
        return new Response<>(unirestResponse);
    }

    public static void shutDownUnirest(){
        Unirest.shutDown();
    }
}

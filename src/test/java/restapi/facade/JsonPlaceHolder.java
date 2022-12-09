package restapi.facade;

import aquality.selenium.browser.AqualityServices;
import restapi.models.Post;
import restapi.models.Response;
import restapi.models.User;
import restapi.util.RestApiRequests;

import java.util.Map;

import static restapi.tests.BaseTest.getTestData;
import static restapi.util.Serialization.serialize;


public class JsonPlaceHolder {
    private JsonPlaceHolder() {}

    private static final String BASE_URL = getTestData().get("restapi").get("baseUrl").asText();

    public static Response<Post> getAllPosts()  {
        String url = BASE_URL + "/posts";
        AqualityServices.getLogger().info("Sending a GET request to " + url);

        return RestApiRequests.get(url);
    }

    public static Response<Post> getPostById(int id)  {
        String url = BASE_URL + "/posts/" + id;
        AqualityServices.getLogger().info("Sending a GET request to " + url);
        return RestApiRequests.get(url);
    }

    public static Response<Post> submitPost(Map<String, String> headers, Post body)  {
        String url = BASE_URL + "/posts";
        String bodyAsString = serialize(body);
        AqualityServices.getLogger().info(String.format("Sending a POST request to %s with params:\n\t"
                + "headers=%s, \n\tbody=%s", url, headers, bodyAsString));
        return RestApiRequests.post(url, bodyAsString, headers);
    }

    public static Response<User> getAllUsers()  {
        String url = BASE_URL + "/users";
        AqualityServices.getLogger().info("Sending a GET request to " + url);
        return RestApiRequests.get(url);
    }

    public static Response<User> getUserByUserId(int userId)  {
        String url = BASE_URL + "/users/" + userId;
        AqualityServices.getLogger().info("Sending a GET request to " + url);
        return RestApiRequests.get(url);
    }
}

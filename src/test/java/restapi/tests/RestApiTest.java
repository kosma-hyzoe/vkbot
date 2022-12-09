package restapi.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import org.testng.annotations.Test;
import restapi.facade.JsonPlaceHolder;
import restapi.models.Post;
import restapi.models.Response;
import restapi.models.User;

import java.net.HttpURLConnection;
import java.util.*;

import static org.testng.Assert.*;
import static restapi.util.Serialization.*;

public class RestApiTest extends BaseTest {
    @Test
    public void jsonPlaceHolderTest() {
        JsonNode testDataJsonNode = getTestData().get("restapi");

        logger.info("Step 1: Posts should be sorted by ID");
        Response<Post> allPostsResponse = JsonPlaceHolder.getAllPosts();
        assertEquals(allPostsResponse.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertTrue(isValidJson(allPostsResponse.getBodyAsString()), "Invalid JSON string");

        ArrayList<Post> allPosts = allPostsResponse.getBodyAsListOfObjects(Post.class);

        boolean areAllPostsSortedById = true;
        Iterator<Post> iterator = allPosts.iterator();
        Post current, previous = iterator.next();
        while (iterator.hasNext()) {
            current = iterator.next();
            if (previous.getId() > current.getId()) {
                areAllPostsSortedById = false;
                break;
            }
            previous = current;
        }

        assertTrue(areAllPostsSortedById, "List of posts is not sorted by ascending ids");


        logger.info("Step 2: Posts content should be correct");
        int postId = testDataJsonNode.get("postContentShouldBeCorrect").get("id").asInt();
        Response <Post> getResponse = JsonPlaceHolder.getPostById(postId);
        assertEquals(getResponse.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertTrue(isValidJson(getResponse.getBodyAsString()), "Invalid JSON string");

        JsonSchema examplePostJsonSchema = getJsonSchema(testDataJsonNode.get("postContentShouldBeCorrect").get("schema"));
        Post examplePost = getResponse.getBodyAsObject(Post.class);
        assertTrue(obeysProvidedSchema(examplePostJsonSchema, examplePost),
                "Object of " + Post.class + " doesn't obey the provided schema");


        logger.info("Step 3: Post should be empty");
        postId = testDataJsonNode.get("postShouldBeEmpty").get("postId").asInt();

        getResponse = JsonPlaceHolder.getPostById(postId);
        assertEquals(getResponse.getResponseCode(), HttpURLConnection.HTTP_NOT_FOUND);

        examplePost = getResponse.getBodyAsObject(Post.class);
        assertNull(examplePost.getBody(), "Post body is not empty");


        logger.info("Step 4:  Submitted post should be correct");
        JsonNode postToSubmitData = testDataJsonNode.get("submittedPostShouldBeCorrect");

        Map<String, String> headers = Map.of(postToSubmitData.get("header").get("key").asText(),
                postToSubmitData.get("header").get("value").asText());
        String postAsString = postToSubmitData.get("content").toString();
        Post postToSubmit = deserialize(postAsString, Post.class);

        Response<Post> postResponse = JsonPlaceHolder.submitPost(headers, postToSubmit);
        assertEquals(postResponse.getResponseCode(), HttpURLConnection.HTTP_CREATED);

        JsonSchema postToSubmitSchema = getJsonSchema(postToSubmitData.get("schema"));
        Post postFromResponse = postResponse.getBodyAsObject(Post.class);
        assertTrue(obeysProvidedSchema(postToSubmitSchema, postFromResponse),
                "Object of " + Post.class + " doesn't obey the provided schema");


        logger.info("Step 5: User info when requesting all users should be correct");
        int requestedUserId = testDataJsonNode.get("requestedUser").get("id").asInt();

        Response<User> allUsersResponse = JsonPlaceHolder.getAllUsers();
        assertEquals(allUsersResponse.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertTrue(isValidJson(allUsersResponse.getBodyAsString()), "Invalid JSON string");

        ArrayList<User> allUsers = allUsersResponse.getBodyAsListOfObjects(User.class);

        User requestedUser = null;
        for (User user : allUsers){
            if (user.getId() == requestedUserId){
                requestedUser = user;
                break;
            }
        }

        JsonSchema requestedUserJsonSchema = getJsonSchema(testDataJsonNode.get("requestedUser").get("schema"));
        assertTrue(obeysProvidedSchema(requestedUserJsonSchema, requestedUser),
                "Object of " + User.class + " doesn't obey the provided schema");


        logger.info("Step 6: User info when requesting that user should be correct");
        Response<User> requestedUserResponse = JsonPlaceHolder.getUserByUserId(requestedUserId);
        assertEquals(requestedUserResponse.getResponseCode(), HttpURLConnection.HTTP_OK);
        assertTrue(isValidJson(requestedUserResponse.getBodyAsString()), "Invalid JSON string");

        requestedUserJsonSchema = getJsonSchema(testDataJsonNode.get("requestedUser").get("schema"));
        requestedUser = requestedUserResponse.getBodyAsObject(User.class);
        assertTrue(obeysProvidedSchema(requestedUserJsonSchema, requestedUser),
                "Object of " + User.class + " doesn't obey the provided schema");
    }

}

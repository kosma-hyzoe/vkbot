package vk;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import aquality.selenium.core.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.*;
import vk.form.FeedPage;
import vk.form.MyProfilePage;
import vk.form.SignInForm;
import vk.model.CommentData;
import vk.model.Credentials;
import vk.model.PostData;
import vk.model.Response;
import vk.util.RestApiRequests;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static org.testng.Assert.assertTrue;
import static vk.util.Serialization.deserialize;
import static vk.util.Serialization.getJsonNode;

public class VkTest {
    private VkTest() {
    }

    public static final Logger logger = AqualityServices.getLogger();
    private static String testData;
    private static Browser browser;
    private static Credentials credentials;

    public static JsonNode getTestData() {
        return getJsonNode(testData);
    }

    @Test
    public void vkTest() {
        String baseUrl = getTestData().get("baseUrl").asText();
        browser.goTo(baseUrl);
        browser.maximize();

        SignInForm signInForm = new SignInForm();
        signInForm.signIn(credentials);

        FeedPage feed = new FeedPage();
        feed.goToMyProfile();

        MyProfilePage myProfilePage = new MyProfilePage(credentials.getUserId());
        myProfilePage.acceptCookies();

        PostData originalPostData = deserialize(getTestData().get("originalPostData").toString(), PostData.class);
        String postOnWallResponseBody = VkRequests.postOnWall(myProfilePage.getOwnerId(), originalPostData,
                credentials.getToken());
        int postId = getJsonNode(postOnWallResponseBody).get("response").get("post_id").asInt();
        originalPostData.setId(postId);
        assertTrue(myProfilePage.isPostDisplayed(originalPostData), "failed to have the post displayed");

        PostData editedPostData = deserialize(getTestData().get("editedPostData").toString(), PostData.class);
        editedPostData.setId(postId);

        VkRequests.editPost(myProfilePage.getOwnerId(), editedPostData, credentials.getToken());
        assertTrue(myProfilePage.isPostDisplayed(editedPostData), "failed to display the edited post");


        String commentMessage = getTestData().get("postCommentMessage").asText();
        CommentData comment = new CommentData(postId, credentials.getUserId(), commentMessage);

        String postCommentOnWallResponseBody = VkRequests.postCommentOnWall(myProfilePage.getOwnerId(), comment,
                credentials.getToken());
        int commentId = getJsonNode(postCommentOnWallResponseBody).get("response").get("comment_id").asInt();
        comment.setId(commentId);
        myProfilePage.showNextCommentOnPost(editedPostData.getId());
        assertTrue(myProfilePage.isCommentDisplayed(comment), "failed to have the comment displayed");

    }


    @BeforeMethod
    public void setUp() {
        try {
            InputStream testDataInputStream = getClass().getClassLoader().getResourceAsStream("testData.local.json");
            assert testDataInputStream != null;
            testData = new String(testDataInputStream.readAllBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        browser = AqualityServices.getBrowser();
        credentials = deserialize(getTestData().get("credentials").toString(), Credentials.class);
    }

    @AfterMethod
    public void tearDown() {
        RestApiRequests.shutDownUnirest();
        browser.quit();
    }

}

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
import vk.util.RestApiRequests;
import vk.util.VkRequests;
import vk.util.VkResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertFalse;
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

        PostData postData = deserialize(getTestData().get("postData").toString(), PostData.class);
        postData.setOwnerId(credentials.getUserId());
        VkResponse postOnWallResponse = VkRequests.postOnWall(postData, credentials.getToken());

        int postId = postOnWallResponse.getItemId("post");
        postData.setId(postId);
        assertTrue(myProfilePage.isPostDisplayed(postData), "failed to have the post displayed");

        PostData postEditionData = deserialize(getTestData().get("postEditionData").toString(), PostData.class);
        PostData editedPostData = postData.getEditedCopy(postEditionData);

        VkRequests.editPost(editedPostData, credentials.getToken());
        assertTrue(myProfilePage.isPostDisplayed(editedPostData), "failed to display the edited post");

        String commentMessage = getTestData().get("postCommentMessage").asText();
        CommentData comment = new CommentData(postId, credentials.getUserId(), commentMessage);
        comment.setOwnerId(credentials.getUserId());

        VkResponse postCommentOnWallResponse = VkRequests.postCommentOnWall(comment, credentials.getToken());
        int commentId = postCommentOnWallResponse.getItemId("comment");
        comment.setId(commentId);
        myProfilePage.showNextCommentOnPost(postId);
        assertTrue(myProfilePage.isCommentDisplayed(comment), "failed to have the comment displayed");

        myProfilePage.likePost(postId);
        // assertTrue(editedPostData.isUserIdInPostLikes(credentials.getUserId(), credentials.getToken()));

        VkRequests.deletePost(credentials.getUserId(), postId, credentials.getToken());
        assertFalse(myProfilePage.isPostDisplayed(postId));

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

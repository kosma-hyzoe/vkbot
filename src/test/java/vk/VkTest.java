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
    public static final Logger logger = AqualityServices.getLogger();
    private static String testData;
    private static Browser browser;
    private static Credentials credentials;

    public static JsonNode getTestData() {
        return getJsonNode(testData);
    }

    @Test
    public void vkUiAndApiIntegration() {
        String baseUrl = getTestData().get("baseUrl").asText();
        browser.goTo(baseUrl);

        logger.info("signing in to VK and opening the feed");
        SignInForm signInForm = new SignInForm();
        signInForm.signIn(credentials);
        FeedPage feed = new FeedPage();

        logger.info("opening the 'My profile' page");
        feed.goToMyProfile();
        MyProfilePage myProfilePage = new MyProfilePage(credentials.getUserId());
        myProfilePage.acceptCookies();

        PostData postData = deserialize(getTestData().get("postData").toString(), PostData.class);
        postData.setOwnerId(credentials.getUserId());

        logger.info("attempting to post a post on a wall via API, expecting it to display in the UI");
        VkResponse postOnWallResponse = VkRequests.postOnWall(postData, credentials.getToken());
        int postId = postOnWallResponse.getItemId("post");
        postData.setId(postId);
        assertTrue(myProfilePage.isPostDisplayed(postData), "failed to display the post");

        PostData postEditionData = deserialize(getTestData().get("postEditionData").toString(), PostData.class);
        PostData editedPostData = postData.getEditedCopy(postEditionData);

        logger.info("attempting to edit the post via API, expecting the changes to be displayed in the UI");
        VkRequests.editPost(editedPostData, credentials.getToken());
        assertTrue(myProfilePage.isPostDisplayed(editedPostData), "failed to display the post changes");

        String commentMessage = getTestData().get("postCommentMessage").asText();
        CommentData comment = new CommentData(postId, credentials.getUserId(), commentMessage);
        comment.setOwnerId(credentials.getUserId());

        logger.info("attempting to create a comment under the post via API, expecting it to display in the UI");
        VkResponse createCommentOnWallResponse = VkRequests.createCommentOnWall(comment, credentials.getToken());
        int commentId = createCommentOnWallResponse.getItemId("comment");
        comment.setId(commentId);
        myProfilePage.showNextCommentOnPost(postId);
        assertTrue(myProfilePage.isCommentMessageDisplayed(comment), "failed to display the comment");

        logger.info("attempting to like post via UI, expecting the user id to appear in likes list requested via API");
        myProfilePage.likePost(postId);
        boolean isUserInPostLikes = editedPostData.isUserIdInPostLikes(credentials.getUserId(), credentials.getToken());
        assertTrue(isUserInPostLikes, "user id not found in post likes list");

        logger.info("attempting to delete the post via API, expecting it not to display in the UI afterwards");
        VkRequests.deletePost(credentials.getUserId(), postId, credentials.getToken());
        assertFalse(myProfilePage.isPostDisplayed(postId), "post still displays - possibly failed to delete");
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
        browser.maximize();
        credentials = deserialize(getTestData().get("credentials").toString(), Credentials.class);
    }

    @AfterMethod
    public void tearDown() {
        RestApiRequests.shutDownUnirest();
        browser.quit();
    }
}

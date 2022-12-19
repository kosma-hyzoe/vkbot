package vk;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import aquality.selenium.core.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.*;
import vk.form.FeedPage;
import vk.form.MyProfilePage;
import vk.form.SignInForm;
import vk.model.Content;
import vk.model.Credentials;
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
    public void myProfileAdministration() {
        String baseUrl = getTestData().get("baseUrl").asText();
        browser.goTo(baseUrl);

        logger.info("signing in to VK and opening the feed");
        SignInForm signInForm = new SignInForm();
        signInForm.signIn(credentials);
        FeedPage feed = new FeedPage();

        logger.info("opening the 'My profile' page, closing cookies if necessary");
        feed.goToMyProfile();
        MyProfilePage myProfilePage = new MyProfilePage(credentials.getUserId());
        myProfilePage.acceptCookies();

        Content post = deserialize(getTestData().get("content").get("post").toString(), Content.class);

        logger.info("attempting to post a post on a wall via API, expecting it to display in the UI");
        VkResponse wallPostResponse = VkRequests.wallPost(post);
        int postId = wallPostResponse.getBody("post");
        assertTrue(myProfilePage.isPostDisplayed(postId), "failed to display the post");

        Content postEdit = deserialize(getTestData().get("content").get("postEdit").toString(), Content.class);

        logger.info("attempting to edit the post via API, expecting the changes to be displayed in the UI");
        VkRequests.wallEditPost(postId, postEdit);
        assertTrue(myProfilePage.isPostDisplayed(postId ,postEdit), "failed to display the post changes");

        Content comment = deserialize(getTestData().get("content").get("comment").toString(), Content.class);

        logger.info("attempting to create a comment under the post via API, expecting it to display in the UI");
        VkResponse wallCreateCommentResponse = VkRequests.wallCreateComment(postId, comment);
        int commentId = wallCreateCommentResponse.getBody("comment");
        myProfilePage.showNextCommentOnPost(postId);
        assertTrue(myProfilePage.isCommentMessageDisplayed(commentId, comment), "failed to display the comment");

        logger.info("attempting to like post via UI, expecting the user id to appear in likes list requested via API");
        myProfilePage.likePost(postId);
        VkResponse likesIsLikedResponse = VkRequests.likesIsLiked(myProfilePage.getOwnerId(), postId, "post");
        boolean isLiked = likesIsLikedResponse.getBody();
        assertTrue(isLiked, "user id not found in post likes list");

        logger.info("attempting to delete the post via API, expecting it not to display in the UI afterwards");
        VkRequests.wallDelete(postId);
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

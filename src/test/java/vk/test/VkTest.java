package vk.test;

import org.testng.annotations.*;
import vk.form.FeedPage;
import vk.form.MyProfilePage;
import vk.form.PostForm;
import vk.form.SignInForm;
import vk.model.Content;
import vk.util.VkRequests;
import vk.model.VkResponse;

import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static vk.util.ConditionalWaits.waitForTrue;
import static vk.util.Serialization.deserialize;

public class VkTest extends BaseTest {

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
        MyProfilePage myProfilePage = new MyProfilePage();
        myProfilePage.acceptCookies();

        Content postContent = deserialize(getTestData().get("content").get("post").toString(), Content.class);

        logger.info("attempting to post a post on a wall via API, expecting it to display in the UI");
        VkResponse wallPostResponse = VkRequests.wallPost(postContent);
        int postId = wallPostResponse.getBody("post");
        PostForm post = new PostForm(credentials.getUserId(), postId);
        boolean isPostDisplayed = waitForTrue(() -> post.state().isDisplayed(), "is post displayed");
        assertTrue(isPostDisplayed, "failed to display the post");


        Content postEditContent = deserialize(getTestData().get("content").get("postEdit").toString(), Content.class);

        logger.info("attempting to edit the post via API, expecting the changes to be displayed in the UI");
        VkRequests.wallEditPost(postId, postEditContent);
        assertTrue(post.isDisplayed(postEditContent), "failed to display post changes");

        Content comment = deserialize(getTestData().get("content").get("comment").toString(), Content.class);

        logger.info("attempting to create a comment under the post via API, expecting it to display in the UI");
        VkResponse wallCreateCommentResponse = VkRequests.wallCreateComment(postId, comment);
        int commentId = wallCreateCommentResponse.getBody("comment");
        post.showNextComment();
        assertTrue(post.isCommentMessageDisplayed(commentId, comment.getMessage()), "failed to display the comment");

        logger.info("attempting to like post via UI, expecting the user id to appear in likes list requested via API");
        post.likePost();
        VkResponse likesIsLikedResponse = VkRequests.likesIsLiked(post.getOwnerId(), postId, "post");
        boolean isLiked = likesIsLikedResponse.getBody();
        assertTrue(isLiked, "user id not found in post likes list");

        logger.info("attempting to delete the post via API, expecting it not to display in the UI afterwards");
        VkRequests.wallDelete(postId);
        boolean isNotPostDisplayed = waitForTrue(() -> !post.state().isDisplayed(), "is not post displayed");
        assertTrue(isNotPostDisplayed, "post still displays - possibly failed to delete");
    }
}

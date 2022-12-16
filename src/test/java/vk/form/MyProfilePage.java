package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import lombok.Getter;
import org.openqa.selenium.By;
import vk.model.CommentData;
import vk.model.PostData;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static vk.VkTest.getTestData;

public class MyProfilePage extends Form {
    @Getter
    private final int ownerId;
    private static final IButton acceptCookies = AqualityServices.getElementFactory().getButton(
            By.xpath("//span[@onclick='hideCookiesPolicy();']"), "'Accept' cookies span");

    public MyProfilePage(int ownerId) {
        super(By.className("ProfileWrapper"), "My profile");
        this.ownerId = ownerId;
    }

    public void acceptCookies() {
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> acceptCookies.state().isClickable(),
                    Duration.ofSeconds(getTestData().get("waits").get("acceptCookiesClickable").get("seconds").asInt()),
                    Duration.ofMillis(getTestData().get("waits").get("acceptCookiesClickable").get("milliseconds").asInt()),
                    "The 'Accept' cookies element should be clickable");
        } catch (TimeoutException e) {
            AqualityServices.getLogger().warn("Accept' cookies element not clickable - skipping...");
            return;
        }
        acceptCookies.click();
    }

    public boolean isPostDisplayed(int postId){
        String postElementId = String.format("post%d_%d", ownerId, postId);
        ILabel post = AqualityServices.getElementFactory().getLabel(By.id(postElementId), "post @ id=" + postId);

        return post.state().isDisplayed();
    }

    public boolean isPostDisplayed(PostData postData) {
        if (postData.getAttachment() == null && postData.getMessage() == null) {
            AqualityServices.getLogger().warn("empty post fields when checking if its displayed - returning false...");
            return false;
        }
        if (postData.getMessage() != null) {
            String rawXpath = "//div[@id='wpt%d_%d']/div[text()='%s']";
            String xpath = String.format(rawXpath, ownerId, postData.getId(), postData.getMessage());
            ITextBox postMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(xpath),
                    "post message: '" + postData.getMessage() + "' @ id=" + postData.getId());
            try {
                AqualityServices.getConditionalWait().waitForTrue(() -> postMessage.state().isDisplayed(),
                        Duration.ofSeconds(getTestData().get("waits").get("postDisplay").get("seconds").asInt()),
                        Duration.ofMillis(getTestData().get("waits").get("postDisplay").get("milliseconds").asInt()),
                        "there should be displayed a post message: " + postData.getMessage());
            } catch (TimeoutException e) {
                AqualityServices.getLogger().info("timeout: failed to display the message within the post");
                return false;
            }
        }
        if (postData.getAttachment() != null) {
            if (!Objects.equals(postData.getAttachment().getType(), "photo")) {
                AqualityServices.getLogger().error("unsupported attachment type");
                return false;
            }

            String rawXpath = "//div[@id='wpt%d_%d']/div/a[@href='/photo%d_%d']";
            String xpath = String.format(rawXpath, ownerId, postData.getId(), postData.getAttachment().getOwnerId(),
                    postData.getAttachment().getMediaId());
            ILabel postAttachment = AqualityServices.getElementFactory().getLabel(By.xpath(xpath),
                    "post attachment with request parameter: " + postData.getAttachment().getAsParameter());
            try {
                AqualityServices.getConditionalWait().waitForTrue(() -> postAttachment.state().isDisplayed(),
                        Duration.ofSeconds(getTestData().get("waits").get("postDisplay").get("seconds").asInt()),
                        Duration.ofMillis(getTestData().get("waits").get("postDisplay").get("milliseconds").asInt()),
                        "a post attachment should display");
            } catch (TimeoutException e) {
                AqualityServices.getLogger().info("timeout: failed to display the attachment of post");
                return false;
            }
        }
        return true;
    }

    public void showNextCommentOnPost(int postId) {
        String xpath = String.format("//div[@id='post%d_%d']//a[span[@class='js-replies_next_label']]", ownerId, postId);
        IButton showNextComment = AqualityServices.getElementFactory().getButton(By.xpath(xpath),
                "'Show next comment' clickable span");
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> showNextComment.state().isClickable(),
                    Duration.ofSeconds(getTestData().get("waits").get("nextCommentClickable").get("seconds").asInt()),
                    Duration.ofMillis(getTestData().get("waits").get("nextCommentClickable").get("milliseconds").asInt()),
                    "'Show next comment' element should be clickable under given post");
        } catch (TimeoutException e) {
            AqualityServices.getLogger().warn("timeout: 'Show next comment' is not clickable - skipping...");
            return;
        }
        showNextComment.click();
    }

    public Boolean isCommentMessageDisplayed(CommentData comment) {
        if (comment.getMessage() == null) {
            AqualityServices.getLogger().warn("empty message field  - returning false");
            return false;
        }

        String rawXpath = "//div[@id='post%d_%d' and @data-answering-id='%d']//div[@class='wall_reply_text' and text()='%s']";
        String xpath = String.format(rawXpath, ownerId, comment.getId(), comment.getAuthorId(), comment.getMessage());
        ITextBox commentMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(xpath), "comment message");
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> commentMessage.state().isDisplayed(),
                    Duration.ofSeconds(getTestData().get("waits").get("postDisplay").get("seconds").asInt()),
                    Duration.ofMillis(getTestData().get("waits").get("postDisplay").get("milliseconds").asInt()),
                    "there should be displayed a comment with a matching message");
        } catch (TimeoutException e) {
            AqualityServices.getLogger().info("timeout: failed to display the comment message");
            return false;
        }
        return true;
    }

    public void likePost(int postId) {
        String rawXpath = "//div[@id='post%d_%d']//div[@class='like_btns']/div[1]";
        String xpath = String.format(rawXpath, ownerId, postId);
        ILabel likeButton = AqualityServices.getElementFactory().getLabel(By.xpath(xpath),
                "like button under post @ id=" + postId);

        likeButton.click();
    }
}

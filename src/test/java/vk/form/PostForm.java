package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import lombok.Getter;
import org.openqa.selenium.By;
import vk.model.Content;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static vk.VkTest.getTestData;

public class PostForm extends Form {
    @Getter
    private final int ownerId;
    @Getter
    private final int id;


    public PostForm(int ownerId, int id) {
        super(By.id(String.format("post%d_%d", ownerId, id)), "post element @ id=" + id);
        this.ownerId = ownerId;
        this.id = id;
    }

    public boolean isDisplayed(Content content) {
        if (content.getAttachment() == null && content.getMessage() == null) {
            AqualityServices.getLogger().warn("empty post fields when checking if displayed - returning false...");
            return false;
        }
        if (content.getMessage() != null) {
            String xpath = "//div[@id='wpt%d_%d']/div[text()='%s']";
            String formattedXpath = String.format(xpath, ownerId, id, content.getMessage());
            ITextBox postMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(formattedXpath),
                    "post message: '" + content.getMessage() + "' @ id=" + id);
            try {
                AqualityServices.getConditionalWait().waitForTrue(() -> postMessage.state().isDisplayed(),
                        Duration.ofSeconds(getTestData().get("waits").get("postDisplay").get("seconds").asInt()),
                        Duration.ofMillis(getTestData().get("waits").get("postDisplay").get("millis").asInt()),
                        "there should be displayed a post message: " + content.getMessage());
            } catch (TimeoutException e) {
                AqualityServices.getLogger().info("timeout: failed to display the message within the post");
                return false;
            }
        }
        if (content.getAttachment() != null) {
            if (!Objects.equals(content.getAttachment().getType(), "photo")) {
                AqualityServices.getLogger().error("unsupported attachment type");
                return false;
            }

            String xpath = "//div[@id='wpt%d_%d']/div/a[@href='/photo%d_%d']";
            String formattedXpath = String.format(xpath, ownerId, id, content.getAttachment().getOwnerId(),
                    content.getAttachment().getMediaId());
            ILabel postAttachment = AqualityServices.getElementFactory().getLabel(By.xpath(formattedXpath),
                    "post attachment with request parameter: " + content.getAttachment().getAsParameter());
            try {
                AqualityServices.getConditionalWait().waitForTrue(() -> postAttachment.state().isDisplayed(),
                        Duration.ofSeconds(getTestData().get("waits").get("postDisplay").get("seconds").asInt()),
                        Duration.ofMillis(getTestData().get("waits").get("postDisplay").get("millis").asInt()),
                        "a post attachment should display");
            } catch (TimeoutException e) {
                AqualityServices.getLogger().info("timeout: failed to display the attachment of post");
                return false;
            }
        }
        return true;
    }

    public void showNextComment() {
        String xpath = String.format("//div[@id='post%d_%d']//a[span[@class='js-replies_next_label']]", ownerId, id);
        IButton showNextComment = AqualityServices.getElementFactory().getButton(By.xpath(xpath),
                "'Show next comment' clickable span");
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> showNextComment.state().isClickable(),
                    Duration.ofSeconds(getTestData().get("waits").get("nextCommentClickable").get("seconds").asInt()),
                    Duration.ofMillis(getTestData().get("waits").get("nextCommentClickable").get("millis").asInt()),
                    "'Show next comment' element should be clickable under given post");
        } catch (TimeoutException e) {
            AqualityServices.getLogger().warn("timeout: 'Show next comment' is not clickable - skipping...");
            return;
        }
        showNextComment.click();
    }

    public boolean isCommentMessageDisplayed(int commentId, String message) {
        String xpath = String.format("//div[@id='post%d_%d' and @data-answering-id='%d']" +
                "//div[@class='wall_reply_text' and text()='%s']", ownerId, commentId, ownerId, message);
        ITextBox commentMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(xpath),
                "comment message @ id=" + id);
        try {
            AqualityServices.getConditionalWait().waitForTrue(() -> commentMessage.state().isDisplayed(),
                    Duration.ofSeconds(getTestData().get("waits").get("postDisplay").get("seconds").asInt()),
                    Duration.ofMillis(getTestData().get("waits").get("postDisplay").get("millis").asInt()),
                    "there should be displayed a comment with a matching message");
        } catch (TimeoutException e) {
            AqualityServices.getLogger().info("timeout: failed to display the comment message");
            return false;
        }
        return true;
    }

    public void likePost() {
        String xpath = "//div[@id='post%d_%d']//div[@class='like_btns']/div[1]";
        String formattedXpath = String.format(xpath, ownerId, id);
        ILabel likeButton = AqualityServices.getElementFactory().getLabel(By.xpath(formattedXpath),
                "like button under post @ id=" + id);

        likeButton.click();
    }
}

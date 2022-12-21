package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import lombok.Getter;
import org.openqa.selenium.By;
import vk.model.Content;
import vk.util.ConditionalWaits;

import java.util.Objects;
import java.util.function.BooleanSupplier;


public class PostForm extends Form {
    @Getter
    private final int ownerId;
    @Getter
    private final int id;
    private final String messageXpath =  "//div[@id='wpt%d_%d']/div[text()='%s']";
    private final String attachmentXpath  = "//div[@id='wpt%d_%d']/div/a[@href='/photo%d_%d']";
    private final String showNextCommentButtonXpath = "//div[@id='post%d_%d']//a[span[@class='js-replies_next_label']]";
    private final String likeButtonXpath = "//div[@id='post%d_%d']//div[@data-section-ref='reactions-button-container']";
    private final String commentMessageXpath = "//div[@id='post%d_%d' and @data-answering-id='%d']" +
                "//div[@class='wall_reply_text' and text()='%s']";


    public PostForm(int ownerId, int id) {
        super(By.id(String.format("post%d_%d", ownerId, id)), "post element @ id=" + id);
        this.ownerId = ownerId;
        this.id = id;
    }

    public boolean isDisplayed(Content content) {
        if (content.getAttachment() == null && content.getMessage() == null) {
            getLogger().warn("empty post fields when checking if displayed - returning false...");
            return false;
        }
        if (content.getMessage() != null) {
            String formattedXpath = String.format(messageXpath, ownerId, id, content.getMessage());
            ITextBox postMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(formattedXpath),
                    "post message: '" + content.getMessage() + "' @ id=" + id);

            BooleanSupplier isPostMessageDisplayed = () -> postMessage.state().isDisplayed();
            return ConditionalWaits.longWaitForTrue(isPostMessageDisplayed, "is post message displayed");
        }
        if (content.getAttachment() != null) {
            if (!Objects.equals(content.getAttachment().getType(), "photo")) {
                getLogger().error("unsupported attachment type");
                return false;
            }

            String formattedXpath = String.format(attachmentXpath, ownerId, id, content.getAttachment().getOwnerId(),
                    content.getAttachment().getMediaId());
            ILabel postAttachment = AqualityServices.getElementFactory().getLabel(By.xpath(formattedXpath),
                    "post attachment with request parameter: " + content.getAttachment().getAsParameter());

            BooleanSupplier isPostAttachmentDisplayed = () -> postAttachment.state().isDisplayed();
            return ConditionalWaits.longWaitForTrue(isPostAttachmentDisplayed, "is post attachment displayed");
        }
        return true;
    }

    public void showNextComment() {
        String formattedXpath = String.format(showNextCommentButtonXpath, ownerId, id);
        IButton showNextComment = AqualityServices.getElementFactory().getButton(By.xpath(formattedXpath),
                "'Show next comment' clickable span");

        BooleanSupplier isShowNextCommentClickable = () -> showNextComment.state().isClickable();
        ConditionalWaits.waitForTrue(isShowNextCommentClickable, "is 'Show next' comment button clickable");

        showNextComment.click();
    }

    public void likePost() {
        String formattedXpath = String.format(likeButtonXpath, ownerId, id);
        ILabel likeButton = AqualityServices.getElementFactory().getLabel(By.xpath(formattedXpath),
                "like button under post @ id=" + id);

        likeButton.click();
    }

    public boolean isCommentMessageDisplayed(int commentId, String message) {
        String formattedXpath = String.format(commentMessageXpath, ownerId, commentId, ownerId, message);
        ITextBox commentMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(formattedXpath),
                "comment message @ id=" + id);

        BooleanSupplier isCommentMessageDisplayed = () -> commentMessage.state().isDisplayed();
        ConditionalWaits.waitForTrue(isCommentMessageDisplayed, "is comment message displayed");
        return true;
    }
}

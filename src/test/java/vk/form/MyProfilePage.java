package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import vk.VkTest;
import vk.model.PostData;

import java.util.Objects;

public class MyProfilePage extends Form {


    public MyProfilePage() {
        super(By.className("ProfileWrapper"), "My profile");
    }


    public boolean isPostDisplayed(PostData postData){
        String authorHeaderXpath = String.format("//h5/a[text()=\"%s\"]", postData.getAuthor());
        ITextBox postAuthorHeader = AqualityServices.getElementFactory().getTextBox(By.xpath(authorHeaderXpath),
                "Post author header: " + postData.getAuthor());

        Boolean isPostAttachmentDisplayed = null;
        if (postData.getAttachment() != null){
            String attachmentType = postData.getAttachment().getType();
            if (!Objects.equals(attachmentType, "photo")){
                AqualityServices.getLogger().error("unsupported attachment type :" + attachmentType);
                return false;
            }
            String attachmentXpath = String.format("//div[@data-photo-id=\"%d_%d\"]",
                    postData.getAttachment().getOwnerId(), postData.getAttachment().getMediaId());
            ITextBox postAttachment = AqualityServices.getElementFactory().getTextBox(By.xpath(attachmentXpath),
                    "Post attachment of parameter: " + postData.getAttachmentParameter());
            isPostAttachmentDisplayed = postAttachment.state().isDisplayed();
        }

        Boolean isPostMessageDisplayed = null;
        if (postData.getMessage() != null){
            String messageXpath = String.format("//div[text()=\"%s\"]", postData.getMessage());
            ITextBox postMessage = AqualityServices.getElementFactory().getTextBox(By.xpath(messageXpath),
                    "Post content with message: " + postData.getMessage());
            isPostMessageDisplayed = postMessage.state().isDisplayed();
        }

        return (isPostMessageDisplayed == null || isPostMessageDisplayed) &&
                (isPostAttachmentDisplayed == null || isPostAttachmentDisplayed);
    }
}

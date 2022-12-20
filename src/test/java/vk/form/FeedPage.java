package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

import java.util.function.BooleanSupplier;

import static vk.util.ConditionalWaits.waitForTrue;

public class FeedPage extends Form {
    private static final ILink myProfile = AqualityServices.getElementFactory().getLink(
            By.xpath("//li[@id='l_pr']/a"), "My profile");

    public FeedPage() {
        super(By.id("side_bar_inner"), "Feed");
    }

    public void goToMyProfile() {
        BooleanSupplier isMyProfileLinkClickable = () -> myProfile.state().isClickable();
        waitForTrue(isMyProfileLinkClickable, "is 'My profile' link clickable");
        myProfile.click();
    }
}

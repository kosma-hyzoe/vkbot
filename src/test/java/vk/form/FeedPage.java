package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static vk.VkTest.getTestData;

public class FeedPage extends Form {
    private static final ILink myProfile = AqualityServices.getElementFactory().getLink(
            By.xpath("//li[@id='l_pr']/a"), "My profile");

    public FeedPage() {
        super(By.id("side_bar_inner"), "Feed");
    }

    public void goToMyProfile(){
        try {
                AqualityServices.getConditionalWait().waitForTrue(() -> myProfile.state().isClickable(),
                        Duration.ofSeconds(getTestData().get("waits").get("myProfileClickable").get("seconds").asInt()),
                        Duration.ofMillis(getTestData().get("waits").get("myProfileClickable").get("millis").asInt()),
                        "'My profile' link should be clickable");
            } catch (TimeoutException e) {
                AqualityServices.getLogger().error("timeout: 'My profile' is still not clickable");
            }
        myProfile.click();
    }
}

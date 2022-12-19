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
                    Duration.ofMillis(getTestData().get("waits").get("acceptCookiesClickable").get("millis").asInt()),
                    "The 'Accept' cookies element should be clickable");
        } catch (TimeoutException e) {
            AqualityServices.getLogger().warn("Accept' cookies element not clickable - skipping...");
            return;
        }
        acceptCookies.click();
    }
}

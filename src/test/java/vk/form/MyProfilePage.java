package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

import java.util.function.BooleanSupplier;

import static vk.util.ConditionalWaits.shortWaitForTrue;

public class MyProfilePage extends Form {
    private static final IButton acceptCookies = AqualityServices.getElementFactory().getButton(
            By.xpath("//span[@onclick='hideCookiesPolicy();']"), "'Accept' cookies span");

    public MyProfilePage() {
        super(By.className("ProfileWrapper"), "My profile");
    }

    public void acceptCookies() {
        BooleanSupplier isAcceptCookiesClickable = () -> acceptCookies.state().isClickable();
        if (shortWaitForTrue(isAcceptCookiesClickable, "Is 'Accept cookies' button clickable")){
            acceptCookies.click();
        }
        return;
    }
}

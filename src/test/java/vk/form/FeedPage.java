package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

public class FeedPage extends Form {
    private static final ITextBox myProfile = AqualityServices.getElementFactory().getTextBox(
            By.id("l_pr"), "My profile");

    public FeedPage() {
        super(By.id("side_bar_inner"), "Feed");
    }

    public void goToMyProfile(){
        myProfile.click();
    }
}

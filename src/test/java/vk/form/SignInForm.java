package vk.form;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import vk.model.Credentials;

public class SignInForm extends Form {
    private final ITextBox phoneOrEmailInputField = AqualityServices.getElementFactory().getTextBox(
        By.id("index_email"), "Phone or email");
    private final IButton signInButton = AqualityServices.getElementFactory().getButton(
            By.xpath("//span[contains(text(),\"Sign in\")]"), "Sign in");
    private final ITextBox passwordInputField = AqualityServices.getElementFactory().getTextBox(
            By.xpath("//input[@name=\"password\"]"), "Enter password");
    private  final IButton continueButton = AqualityServices.getElementFactory().getButton(
            By.xpath("//span[text()=\"Continue\"]"), "Continue");


    public SignInForm() {
        super(By.id("index_login"), "Sign in form");
    }

    public void signIn(Credentials credentials){
        phoneOrEmailInputField.clearAndType(credentials.getPhone());
        signInButton.click();
        passwordInputField.clearAndType(credentials.getPassword());
        continueButton.click();

    }
}

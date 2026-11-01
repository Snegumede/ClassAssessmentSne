package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;

    @FindBy(id = "login-email")
    WebElement usernameField;

    @FindBy(id = "login-password")
    WebElement passwordField;

    @FindBy(id = "login-submit")
    WebElement loginSubmitButton;


    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {

        passwordField.sendKeys(password);
    }

    public void clickLoginSubmitButton() {

        loginSubmitButton.click();
    }

}



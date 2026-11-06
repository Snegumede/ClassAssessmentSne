package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LandingPage {

    WebDriver driver;

    @FindBy(xpath = "//span[normalize-space()='Learn']")
    WebElement LearnTabDropdown;

    @FindBy(xpath ="//span[normalize-space()='Learning Materials']")
    WebElement LearningMaterialsButton;
    @FindBy(xpath = "//span[normalize-space()='My Learning']")
    WebElement myLearningTab;

    @FindBy(xpath = "//span[contains(normalize-space(),'Web Automation Advance')] | //*[@role='tab'][contains(normalize-space(),'Web Automation Advance')] | //a[contains(normalize-space(),'Web Automation Advance')]")
    WebElement webAutomationAdvanceTab;

    @FindBy(xpath = "//span[normalize-space()='Menu']")
    WebElement menuTab;
    @FindBy(xpath = "//span[normalize-space()='Logout']")
    WebElement logoutButton;

    public LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean verifyLoginSuccess() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOf(myLearningTab));
        return myLearningTab.isDisplayed();
    }

    public void clickLearnTabDropdown() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.elementToBeClickable(LearnTabDropdown));
        LearnTabDropdown.click();
    }

    public void clickLearningMaterialsButton() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.elementToBeClickable(LearningMaterialsButton));
        LearningMaterialsButton.click();
    }

    public void clickWebAutomationAdvanceTab() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.elementToBeClickable(webAutomationAdvanceTab));
        webAutomationAdvanceTab.click();
    }
    public void clickMenuButton(){
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOf(menuTab));
        menuTab.click();
    }
    public  void clickLogoutButton(){
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(ExpectedConditions.visibilityOf(logoutButton));
        logoutButton.click();
    }
    public void acceptAlert(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

}

package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Step 1 of the "Web Automation Advance" flow: the Inventory Form. */
public class InventoryFormPage extends BasePage {

    @FindBy(xpath = "//button[normalize-space()='Next'] | //span[normalize-space()='Next']")
    WebElement nextButton;

    private static final By FORM_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3 or self::h4 or self::legend][contains(normalize-space(),'Inventory')]"
                    + " | //label[contains(normalize-space(),'Device Type')]");

    private static final By DEVICE_PREVIEW = By.xpath(
            "//*[contains(translate(@class,'PREVIEW','preview'),'preview')"
                    + " or contains(translate(@id,'PREVIEW','preview'),'preview')"
                    + " or contains(translate(@data-testid,'PREVIEW','preview'),'preview')"
                    + " or contains(normalize-space(text()),'Device Preview')]");

    public InventoryFormPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInventoryFormDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_HEADING)).isDisplayed();
    }

    public void selectDeviceType(String deviceType) {
        choose("Device Type", deviceType);
    }

    /** The Brand dropdown must become enabled once a device type has been chosen. */
    public boolean isBrandEnabled() {
        try {
            WebElement brand = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(fieldBy("Brand", ANY_FIELD)));
            return wait.until(d -> isControlEnabled(brand));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void selectBrand(String brand) {
        choose("Brand", brand);
    }

    public boolean isDevicePreviewDisplayed() {
        try {
            return waitForVisibleWithText(DEVICE_PREVIEW).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void selectStorage(String storage) {
        choose("Storage", storage);
    }

    /** Checks a price (e.g. R480.00) is shown on the page. */
    public boolean isPriceDisplayed(String price) {
        return pageContainsText(price);
    }

    public void selectColor(String color) {
        choose("Color", color);
    }

    public boolean isColorApplied(String color) {
        return pageContainsText(color);
    }

    public void enterQuantity(String quantity) {
        type(findField("Quantity"), quantity);
    }

    public void enterAddress(String address) {
        type(findField("Address"), address);
    }

    public String getAddressValue() {
        return findField("Address").getDomProperty("value");
    }

    public void clickNext() {
        click(nextButton);
    }

}

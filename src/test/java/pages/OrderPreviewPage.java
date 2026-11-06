package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Step 2: order preview - shipping, warranty, discount code and purchase confirmation. */
public class OrderPreviewPage extends BasePage {

    @FindBy(xpath = "//button[normalize-space()='Apply'] | //button[contains(normalize-space(),'Apply')] | //span[normalize-space()='Apply']")
    WebElement applyDiscountButton;

    @FindBy(xpath = "//button[normalize-space()='Confirm Purchase'] | //span[normalize-space()='Confirm Purchase']")
    WebElement confirmPurchaseButton;

    private static final By ORDER_PREVIEW = By.xpath(
            "//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5][contains(normalize-space(),'Order')]"
                    + " | //*[contains(normalize-space(text()),'Express Shipping')]");

    public OrderPreviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderPreviewDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ORDER_PREVIEW)).isDisplayed();
    }

    public void selectShipping(String shippingOption) {
        chooseByText(shippingOption);
    }

    public void selectWarranty(String warrantyOption) {
        chooseByText(warrantyOption);
    }

    /** Checks an amount (e.g. R25.00) is shown on the page. */
    public boolean isAmountDisplayed(String amount) {
        return pageContainsText(amount);
    }

    public void applyDiscountCode(String code) {
        type(findField("Discount"), code);
        click(applyDiscountButton);
    }

    public boolean isDiscountApplied(String expectedDiscount) {
        return pageContainsText(expectedDiscount);
    }

    public void clickConfirmPurchase() {
        click(confirmPurchaseButton);
    }

}

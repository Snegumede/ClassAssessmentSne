package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/** Step 3: success toast, invoice history panel and the invoice itself. */
public class InvoicePage extends BasePage {

    @FindBy(xpath = "//button[normalize-space()='View Invoice'] | //span[normalize-space()='View Invoice']")
    WebElement viewInvoiceButton;

    /** First "View" button in the invoice history list. */
    @FindBy(xpath = "(//button[normalize-space()='View'] | //a[normalize-space()='View'] | //span[normalize-space()='View'])[1]")
    WebElement viewOnInvoiceButton;

    private static final By TOAST = By.xpath(
            "//*[@role='alert' or @role='status'"
                    + " or contains(@class,'toast') or contains(@class,'Toast')"
                    + " or contains(@class,'snackbar') or contains(@class,'Snackbar')"
                    + " or contains(@class,'notification')]");

    private static final By INVOICE_HISTORY = By.xpath(
            "//*[contains(normalize-space(text()),'Invoice History') or contains(normalize-space(text()),'Invoices')]");

    private static final By INVOICE_CONTAINER = By.xpath(
            "//*[@role='dialog' or contains(@class,'invoice') or contains(@class,'Invoice') or contains(@class,'modal')]");

    public InvoicePage(WebDriver driver) {
        super(driver);
    }

    /** Returns the toast text once it appears (empty string if none showed up). */
    public String getSuccessToastText() {
        try {
            return waitForVisibleWithText(TOAST).getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    public void clickViewInvoice() {
        click(viewInvoiceButton);
    }

    public boolean isInvoiceHistoryDisplayed() {
        try {
            return waitForVisibleWithText(INVOICE_HISTORY).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Opens the invoice; copes with it opening in a modal, in the same page or in a new tab. */
    public void clickViewOnInvoice() {
        Set<String> handlesBefore = driver.getWindowHandles();
        click(viewOnInvoiceButton);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(d -> d.getWindowHandles().size() > handlesBefore.size());
            for (String handle : driver.getWindowHandles()) {
                if (!handlesBefore.contains(handle)) {
                    driver.switchTo().window(handle);
                }
            }
        } catch (TimeoutException ignored) {
            // invoice opened in a modal / the same tab
        }
    }

    /** Text of the open invoice (the dialog/invoice container if there is one, otherwise the whole page). */
    public String getInvoiceText() {
        pageContainsText("Invoice");
        List<WebElement> containers = driver.findElements(INVOICE_CONTAINER);
        for (WebElement container : containers) {
            if (container.isDisplayed() && !container.getText().isBlank()) {
                return container.getText();
            }
        }
        return driver.findElement(By.tagName("body")).getText();
    }

}

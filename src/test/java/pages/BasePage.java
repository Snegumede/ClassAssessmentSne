package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Shared helpers for the page objects. All the "how do I find / interact with this kind of
 * control" logic lives here so the page classes only describe WHAT the user does.
 */
public abstract class BasePage {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-_ ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";

    /** Any form control that can hold a value. */
    protected static final String ANY_FIELD = "self::select or self::input or self::textarea or @role='combobox'";
    /** Controls that open a list of options (native select or custom dropdown). */
    protected static final String DROPDOWN_FIELD = "self::select or @role='combobox' or @aria-haspopup='listbox'";

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    // ---------------------------------------------------------------- basic actions

    protected void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        scrollTo(element);
        element.click();
    }

    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        scrollTo(element);
        element.click();
        element.clear();
        // React controlled inputs do not always notice clear(), so select-all + delete as well
        Keys modifier = System.getProperty("os.name").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL;
        element.sendKeys(Keys.chord(modifier, "a"), Keys.DELETE);
        element.sendKeys(text);
    }

    protected boolean isControlEnabled(WebElement element) {
        String cssClass = element.getDomAttribute("class");
        return element.isEnabled()
                && !"true".equalsIgnoreCase(element.getDomAttribute("aria-disabled"))
                && (cssClass == null || !cssClass.toLowerCase().contains("disabled"));
    }

    /** True once the visible page text contains the given text (e.g. a price such as R960.00). */
    protected boolean pageContainsText(String text) {
        try {
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), text));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Waits for the first element matching the locator that is displayed and has some text. */
    protected WebElement waitForVisibleWithText(By locator) {
        return wait.until(d -> d.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .filter(e -> !e.getText().isBlank())
                .findFirst()
                .orElse(null));
    }

    // ---------------------------------------------------------------- finding form fields

    private static String attrContains(String attribute, String key) {
        return "contains(translate(@" + attribute + ",'" + UPPER + "','" + LOWER + "'),'" + key + "')";
    }

    /**
     * Finds a form control from its visible label (e.g. "Brand"). Tries, in this order:
     * the control the label points to (label/@for), the next control after the label,
     * then any control whose id / name / placeholder / data-testid / aria-label contains the label.
     */
    protected By fieldBy(String label, String fieldCondition) {
        String key = label.toLowerCase().replace(" ", "");
        String labelXpath = "//label[contains(normalize-space(),'" + label + "')]";
        String byAttribute = "//*[(" + fieldCondition + ") and ("
                + attrContains("id", key) + " or "
                + attrContains("name", key) + " or "
                + attrContains("placeholder", key) + " or "
                + attrContains("data-testid", key) + " or "
                + attrContains("aria-label", key) + ")]";
        return By.xpath("//*[@id=" + labelXpath + "/@for]"
                + " | " + labelXpath + "/following::*[" + fieldCondition + "][1]"
                + " | " + byAttribute);
    }

    protected WebElement findField(String label) {
        WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(fieldBy(label, ANY_FIELD)));
        scrollTo(field);
        return field;
    }

    // ---------------------------------------------------------------- choosing options

    /**
     * Picks an option for a field, whatever the UI widget turns out to be:
     * native select, custom dropdown (click, then pick option), or radio buttons / swatches / cards.
     */
    protected void choose(String fieldLabel, String optionText) {
        WebElement dropdown = null;
        try {
            dropdown = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.presenceOfElementLocated(fieldBy(fieldLabel, DROPDOWN_FIELD)));
        } catch (TimeoutException ignored) {
            // not a dropdown - fall through to clicking the option directly
        }

        if (dropdown == null) {
            chooseByText(optionText);
        } else if ("select".equalsIgnoreCase(dropdown.getTagName())) {
            selectNative(dropdown, optionText);
        } else {
            click(dropdown);
            chooseByText(optionText);
        }
    }

    private void selectNative(WebElement selectElement, String optionText) {
        wait.until(d -> new Select(selectElement).getOptions().size() > 1);
        Select select = new Select(selectElement);
        try {
            select.selectByVisibleText(optionText);
        } catch (NoSuchElementException e) {
            for (WebElement option : select.getOptions()) {
                if (option.getText().contains(optionText)) {
                    select.selectByVisibleText(option.getText());
                    return;
                }
            }
            throw e;
        }
    }

    /** Clicks the first visible option / radio / label / button / card showing this text (exact match first). */
    protected void chooseByText(String text) {
        try {
            wait.until(d -> clickFirstVisibleMatch(text));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("No clickable option with text '" + text + "' was found", e);
        }
    }

    private boolean clickFirstVisibleMatch(String text) {
        String[] conditions = {
                "normalize-space()='" + text + "'",
                "contains(normalize-space(),'" + text + "')"
        };
        String[] templates = {
                "//*[@role='option' or @role='radio' or @role='checkbox' or @role='menuitem' or self::li][%s]",
                "//label[%s]",
                "//button[%s]",
                "//*[self::span or self::div or self::p][%s]"
        };
        for (String condition : conditions) {
            for (String template : templates) {
                for (WebElement element : driver.findElements(By.xpath(String.format(template, condition)))) {
                    try {
                        if (element.isDisplayed()) {
                            scrollTo(element);
                            element.click();
                            return true;
                        }
                    } catch (WebDriverException ignored) {
                        // stale or covered - try the next candidate
                    }
                }
            }
        }
        return false;
    }

}

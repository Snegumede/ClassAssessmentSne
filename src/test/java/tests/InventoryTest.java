package tests;

import extentReports.Listener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.ReadData;

/**
 * End-to-end flow: login -> Web Automation Advance -> Inventory Form -> order preview
 * -> confirm purchase -> invoice. The steps share one browser session, so each test
 * depends on the one before it (if a step fails, the later ones are skipped).
 */
@Listeners(Listener.class)
public class InventoryTest extends Base {

    @Test(priority = 1)
    public void validLoginTest() {
        takeScreenshots.takeSnapShot(driver, "BeforeLogin");
        homepage.clickLoginButton();
        loginPage.enterUsername(ReadData.username);
        loginPage.enterPassword(ReadData.password);
        loginPage.clickLoginSubmitButton();
        Assert.assertTrue(landingPage.verifyLoginSuccess(), "User should be logged in (My Learning tab visible)");
        takeScreenshots.takeSnapShot(driver, "AfterLogin");
    }

    @Test(priority = 2, dependsOnMethods = "validLoginTest")
    public void inventoryFormIsShownTest() {
        landingPage.clickLearnTabDropdown();
        landingPage.clickLearningMaterialsButton();
        landingPage.clickWebAutomationAdvanceTab();
        Assert.assertTrue(inventoryFormPage.isInventoryFormDisplayed(), "Inventory Form should be displayed");
        takeScreenshots.takeSnapShot(driver, "InventoryForm");
    }

    @Test(priority = 3, dependsOnMethods = "inventoryFormIsShownTest")
    public void selectDeviceTypeTest() {
        inventoryFormPage.selectDeviceType(ReadData.get("device type"));
        Assert.assertTrue(inventoryFormPage.isBrandEnabled(), "Brand dropdown should be enabled after choosing a device type");
    }

    @Test(priority = 4, dependsOnMethods = "selectDeviceTypeTest")
    public void selectBrandTest() {
        inventoryFormPage.selectBrand(ReadData.get("brand"));
        Assert.assertTrue(inventoryFormPage.isDevicePreviewDisplayed(), "Device preview should be displayed");
        takeScreenshots.takeSnapShot(driver, "DevicePreview");
    }

    @Test(priority = 5, dependsOnMethods = "selectBrandTest")
    public void selectStorageTest() {
        inventoryFormPage.selectStorage(ReadData.get("storage"));
        Assert.assertTrue(inventoryFormPage.isPriceDisplayed(ReadData.get("expected unit price")),
                "Unit price " + ReadData.get("expected unit price") + " should be displayed");
    }

    @Test(priority = 6, dependsOnMethods = "selectStorageTest")
    public void selectColorTest() {
        inventoryFormPage.selectColor(ReadData.get("color"));
        Assert.assertTrue(inventoryFormPage.isColorApplied(ReadData.get("color")),
                "Color " + ReadData.get("color") + " should be applied");
    }

    @Test(priority = 7, dependsOnMethods = "selectColorTest")
    public void enterQuantityTest() {
        inventoryFormPage.enterQuantity(ReadData.get("quantity"));
        Assert.assertTrue(inventoryFormPage.isPriceDisplayed(ReadData.get("expected subtotal")),
                "Subtotal " + ReadData.get("expected subtotal") + " should be displayed");
    }

    @Test(priority = 8, dependsOnMethods = "enterQuantityTest")
    public void enterAddressTest() {
        inventoryFormPage.enterAddress(ReadData.get("address"));
        Assert.assertEquals(inventoryFormPage.getAddressValue(), ReadData.get("address"), "Address field should be filled");
        takeScreenshots.takeSnapShot(driver, "InventoryFormFilled");
    }

    @Test(priority = 9, dependsOnMethods = "enterAddressTest")
    public void clickNextTest() {
        inventoryFormPage.clickNext();
        Assert.assertTrue(orderPreviewPage.isOrderPreviewDisplayed(), "Order preview should be displayed");
        takeScreenshots.takeSnapShot(driver, "OrderPreview");
    }

    @Test(priority = 10, dependsOnMethods = "clickNextTest")
    public void selectExpressShippingTest() {
        orderPreviewPage.selectShipping(ReadData.get("shipping"));
        Assert.assertTrue(orderPreviewPage.isAmountDisplayed(ReadData.get("expected shipping cost")),
                "Shipping cost " + ReadData.get("expected shipping cost") + " should be added");
    }

    @Test(priority = 11, dependsOnMethods = "selectExpressShippingTest")
    public void selectWarrantyTest() {
        orderPreviewPage.selectWarranty(ReadData.get("warranty"));
        Assert.assertTrue(orderPreviewPage.isAmountDisplayed(ReadData.get("expected warranty cost")),
                "Warranty cost " + ReadData.get("expected warranty cost") + " should be added");
    }

    @Test(priority = 12, dependsOnMethods = "selectWarrantyTest")
    public void applyDiscountCodeTest() {
        orderPreviewPage.applyDiscountCode(ReadData.get("discount code"));
        Assert.assertTrue(orderPreviewPage.isDiscountApplied(ReadData.get("expected discount")),
                ReadData.get("expected discount") + " discount should be applied");
        takeScreenshots.takeSnapShot(driver, "DiscountApplied");
    }

    @Test(priority = 13, dependsOnMethods = "applyDiscountCodeTest")
    public void confirmPurchaseTest() {
        orderPreviewPage.clickConfirmPurchase();
        String toastText = invoicePage.getSuccessToastText();
        takeScreenshots.takeSnapShot(driver, "PurchaseConfirmed");
        Assert.assertFalse(toastText.isEmpty(), "A success toast should be displayed");
        Assert.assertTrue(toastText.toLowerCase().matches("(?s).*(success|confirmed|order).*"),
                "Toast should confirm the order, but said: " + toastText);
    }

    @Test(priority = 14, dependsOnMethods = "confirmPurchaseTest")
    public void viewInvoiceHistoryTest() {
        invoicePage.clickViewInvoice();
        Assert.assertTrue(invoicePage.isInvoiceHistoryDisplayed(), "Invoice history panel should be displayed");
        takeScreenshots.takeSnapShot(driver, "InvoiceHistory");
    }

    @Test(priority = 15, dependsOnMethods = "viewInvoiceHistoryTest")
    public void viewInvoiceDetailsTest() {
        invoicePage.clickViewOnInvoice();
        String invoiceText = invoicePage.getInvoiceText().toLowerCase();
        takeScreenshots.takeSnapShot(driver, "InvoiceDetails");

        SoftAssert softAssert = new SoftAssert();
        String[] expectedDetails = {
                ReadData.get("brand"),
                ReadData.get("storage"),
                ReadData.get("color"),
                ReadData.get("quantity"),
                ReadData.get("address"),
                ReadData.get("expected subtotal")
        };
        for (String detail : expectedDetails) {
            softAssert.assertTrue(invoiceText.contains(detail.toLowerCase()), "Invoice should contain: " + detail);
        }
        softAssert.assertAll();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

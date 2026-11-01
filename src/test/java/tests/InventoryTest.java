package tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class InventoryTest extends Base {
    @Test
    public void validLoginTest() {
        takeScreenshots.takeSnapShot(driver, "BeforeLogin");
        homepage.clickLoginButton();
        loginPage.enterUsername(readData.username);
        loginPage.enterPassword(readData.password);
        loginPage.clickLoginSubmitButton();
        landingPage.verifyLoginSuccess();
        takeScreenshots.takeSnapShot(driver, "AfterLogin");
        landingPage.clickMenuButton();
        landingPage.clickLogoutButton();
        landingPage.acceptAlert();

    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

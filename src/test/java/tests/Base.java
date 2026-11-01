package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import pages.HomePage;
import pages.LandingPage;
import pages.LoginPage;
import utils.BrowserFactory;
/*import utils.ReadData;
import utils.TakeScreenshots;*/

public class Base {
    BrowserFactory browserFactory = new BrowserFactory();

    final WebDriver driver = browserFactory.startBrowser("chrome", "https://ndosisimplifiedautomation.vercel.app/");

    public HomePage homepage = PageFactory.initElements(driver, HomePage.class);
    public LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
    public LandingPage landingPage = PageFactory.initElements(driver, LandingPage.class);
    /*public TakeScreenshots takeScreenshots = new TakeScreenshots();

    ReadData readData;

    {
        try {
            readData = new ReadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

}

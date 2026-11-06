package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import pages.*;
import utils.BrowserFactory;
import utils.ReadData;
import utils.TakeScreenshots;
//import utils.TakeScreenshots;

public class Base {
    BrowserFactory browserFactory = new BrowserFactory();

    final WebDriver driver = browserFactory.startBrowser("edge", "https://ndosisimplifiedautomation.vercel.app/");

    public HomePage homepage = PageFactory.initElements(driver, HomePage.class);
    public LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
    public LandingPage landingPage = PageFactory.initElements(driver, LandingPage.class);
    public InventoryFormPage inventoryFormPage = PageFactory.initElements(driver, InventoryFormPage.class);
    public OrderPreviewPage orderPreviewPage = PageFactory.initElements(driver, OrderPreviewPage.class);
    public InvoicePage invoicePage = PageFactory.initElements(driver, InvoicePage.class);
    public TakeScreenshots takeScreenshots = new TakeScreenshots();

    ReadData readData;

    {
        try {
            readData = new ReadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

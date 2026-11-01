package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserFactory {

    static WebDriver driver;

    public static WebDriver startBrowser(String browserChoice, String url){
        if (browserChoice.equalsIgnoreCase("Chrome")) {
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless=new");
//            driver = new ChromeDriver(options);
            driver = new ChromeDriver();
        }else if (browserChoice.equalsIgnoreCase("FireFOx")) {
            driver = new FirefoxDriver();
        } else {
            driver = new EdgeDriver();
        }
        driver.manage().window().maximize();
        driver.get(url);
     //   driver.manage().window().setSize(new Dimension(1920,1080));
        return driver;
    }
}

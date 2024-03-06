package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.Test;

public class MyCustomDriverManager {

    protected WebDriver driver;

    public  WebDriver getDriver(BrowserType browserType){
        switch (browserType) {
            case CHROME -> {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }
            case SAFARI -> {
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
            }
            case OPERA -> {
                WebDriverManager.operadriver().setup();
                driver = new SafariDriver();
            }
            case FIREFOX -> {
                WebDriverManager.firefoxdriver().setup();
                driver = new SafariDriver();
            }
            case EDGE -> {
                WebDriverManager.edgedriver().setup();
                driver = new SafariDriver();
            }
        }

        return driver;
    }

    public enum BrowserType{
        CHROME,
        SAFARI,
        OPERA,
        FIREFOX,
        EDGE
    }


//    public static void main(String[] args) {
//       new MyCustomDriverManager().getDriver(BrowserType.SAFARI);
//    }
}



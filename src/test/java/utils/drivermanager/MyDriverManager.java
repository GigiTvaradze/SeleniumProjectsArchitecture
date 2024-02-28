package utils.drivermanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ConfigProperties;

import java.net.MalformedURLException;

public class MyDriverManager {
    public static WebDriver getDriver(DriverSetUp setUp) throws MalformedURLException {
        System.setProperty(ConfigProperties.getProperty("chromeDriverProperty"), ConfigProperties.getProperty("chromeDriverPath"));
        switch (setUp) {
            case VISUAL:
                return visualSetUp();
            case HEADLESS:
                return headlessSetUp();
            default:
                throw new IllegalArgumentException("illegal driver set up type: " + setUp);
        }
    }

    public static WebDriver visualSetUp() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    public static WebDriver headlessSetUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        return new ChromeDriver(options);
    }
}
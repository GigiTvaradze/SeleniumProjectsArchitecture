package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import utils.ActionDriver;
import utils.ConfigProperties;
import utils.MyCustomDriverManager;
import utils.drivermanager.DriverSetUp;
import utils.drivermanager.MyDriverManager;
import utils.logger.ActionLogger;
import utils.logger.MyLogManager;

import java.net.MalformedURLException;
import java.time.Duration;

public class BaseTest {
    protected  WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected ActionLogger actionLogger;
    protected ActionDriver actionDriver;
    public static ThreadLocal<ActionDriver> driverThreadLocal = new ThreadLocal<>();
    public SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup(ITestResult result) throws MalformedURLException {
        //driver.manage().deleteAllCookies();

//        driver = MyDriverManager.getDriver(DriverSetUp.HEADLESS);
        driver = MyDriverManager.getDriver(MyCustomDriverManager.BrowserType.CHROME,DriverSetUp.VISUAL);


        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigProperties.getProperty("webDriverWaitDuration"))));
        js = (JavascriptExecutor) driver;
        softAssert = new SoftAssert();

        actionLogger = MyLogManager.getActionLogger(
                result.getMethod().getConstructorOrMethod().getMethod(),
                result.getMethod().getCurrentInvocationCount());

        actionDriver = new ActionDriver(driver, actionLogger);
        driverThreadLocal.set(actionDriver);

        driver.get(ConfigProperties.getProperty("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void createReportAndCloseSession(ITestResult result) {
        tearDown();
    }

    public void tearDown() {
        if (driver != null) {
            //driver.close();
            driver.quit();
        }
    }
}
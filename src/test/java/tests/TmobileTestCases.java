package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pajeobjects.forms.TestCasesForm;
import pajeobjects.forms.TmobileTestForm;

public class TmobileTestCases extends BaseTest{

    //Go To https://www.t-mobile.com/
    //Hover to Phone&devices
    //hover and click on Tablets
    //Write functions which get filters types and takes specific filter parameters

    @Test(priority = 1,invocationCount = 10)
    public void test1(){
        TmobileTestForm tmobiletestform = new TmobileTestForm(actionDriver,wait,js);

        //visibilityOfElementLocated
        Actions action = new Actions(driver);
        WebElement phonesDevices = driver.findElement(By.xpath("//a[@data-analytics-navigation-name='Phones & devices']"));
        action.moveToElement(phonesDevices).build().perform();
        WebElement tablets = driver.findElement(By.xpath("//a[@data-analytics-navigation-name='Phones & devices|Tablets']"));
        action.moveToElement(tablets).click().build().perform();

        Assert.assertTrue(tmobiletestform.TabletsPageIsDisplayed());
    }
}

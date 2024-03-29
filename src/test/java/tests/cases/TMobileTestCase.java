package tests.cases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pajeobjects.forms.TMobileTestForm;
import tests.BaseTest;
import utils.WebDriverUtils;

public class TMobileTestCase extends BaseTest {

    //Go To https://www.t-mobile.com/
    //Hover to Phone&devices
    //hover and click on Tablets
    //write function which navigates to specific menu and submenu
    //Write function which get filters types and takes specific filter parameters

    @Test(priority = 1,invocationCount = 1)
    public void testTMobileTabletsPageFilter() throws InterruptedException {
        TMobileTestForm tmobiletestform = new TMobileTestForm(actionDriver,wait,js);
        tmobiletestform.navigateToMenu("Phones & devices");
        tmobiletestform.navigateToSubMenu("Phones & devices|Tablets");

        Assert.assertTrue(tmobiletestform.TabletsPageIsDisplayed());

        tmobiletestform.selectFilter("Deals",
                                     "New");
        //WebDriverUtils.captureScreenshot(driver,"Deals Screen");
        tmobiletestform.selectFilter("Brands",
                                     "Apple", "Samsung", "TCL");
        //WebDriverUtils.captureScreenshot(driver,"Brands Screen");
        tmobiletestform.selectFilter("Operating System",
                                     "Other");
        //WebDriverUtils.captureScreenshot(driver,"Operating System Screen");
    }
}

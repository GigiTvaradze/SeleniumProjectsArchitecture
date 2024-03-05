package tests.cases;

import org.testng.Assert;
import org.testng.annotations.Test;
import pajeobjects.forms.TMobileTestForm;
import tests.BaseTest;

public class TMobileTestCase extends BaseTest {

    //Go To https://www.t-mobile.com/
    //Hover to Phone&devices
    //hover and click on Tablets
    //write function which navigates to specific menu and submenu
    //Write function which get filters types and takes specific filter parameters

    @Test(priority = 1,invocationCount = 10)
    public void testTMobileTabletsPageFilter() throws InterruptedException {
        TMobileTestForm tmobiletestform = new TMobileTestForm(actionDriver,wait,js);
        tmobiletestform.navigateToMenu("Phones & devices");
        tmobiletestform.navigateToSubMenu("Phones & devices|Tablets");

        Assert.assertTrue(tmobiletestform.TabletsPageIsDisplayed());

        tmobiletestform.selectFilter("Deals",
                                     "Special offer");
        tmobiletestform.selectFilter("Brands",
                                     "Apple", "Samsung", "T-Mobile");
        tmobiletestform.selectFilter("Operating System",
                                     "Other");
    }
}

package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pajeobjects.forms.TestCasesForm;

public class TestCases extends BaseTest{

    //Test Case Description:
    //Go to Google.com
    //search FlashScore
    //Go to FlashScore
    //Search 'Barcelona'
    //Go To Fixture
    //Print next game of Barcelona
    @Test
    public void test1(){
        TestCasesForm testCasesForm = new TestCasesForm(actionDriver,wait,js);
        testCasesForm.setString("Flashscore");

        TestCasesForm.selectOptionByText(driver,wait,"flashscore football");
        Assert.assertEquals(testCasesForm.getValue(),"Flashscore.com: Football Live Scores, Latest Football Results");
    }
}

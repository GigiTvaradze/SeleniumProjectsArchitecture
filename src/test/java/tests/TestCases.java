package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pajeobjects.TestCasesForm;

public class TestCases extends BaseTest{
    @Test(priority = 1)
    public void test1(){
        TestCasesForm testCasesForm = new TestCasesForm(actionDriver,wait,js);

        testCasesForm.setString("");
        TestCasesForm.selectOptionByText(driver,wait,"");
        Assert.assertEquals(testCasesForm.getValue(),"");
    }
}

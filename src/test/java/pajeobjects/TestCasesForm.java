package pajeobjects;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pajeobjects.BasePage;
import utils.ActionDriver;

import java.util.List;

public class TestCasesForm extends BasePage {
    public TestCasesForm(ActionDriver actionDriver, WebDriverWait wait, JavascriptExecutor js){
        super(actionDriver,wait,js);
    }

    By clickLocator = By.id("");
    By setSearchStringLocator = By.id("");
    By IsDisplayedLocator = By.id("");
    By getValueLocator = By.xpath("");
    public void click() {
      actionDriver.explicitClickByVisibility(wait, clickLocator);
    }
    public void setString(String value) {
        actionDriver.usualClear(wait, setSearchStringLocator);
        actionDriver.sendKeysByVisibility(wait, setSearchStringLocator, value);
    }
    public boolean IsDisplayed() {
        return actionDriver.elementIsVisible(IsDisplayedLocator, 5);
    }
    public String getValue() {
        return actionDriver.getTextByVisibility(wait, getValueLocator);
    }

    public static void selectOptionByText(WebDriver driver, WebDriverWait wait, String desiredText) {
        // Wait for the listBox to be visible
        WebElement listBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[role='listbox']")));

        // Find options within the listBox
        List<WebElement> options = listBox.findElements(By.tagName("li"));

        // Iterate through options and print their text
        for (WebElement option : options) {
            System.out.println(option.getText());
        }

        // Iterate through options to find the desired one
        WebElement desiredOption = null;
        for (WebElement option : options) {
            if (option.getText().equals(desiredText)) {
                desiredOption = option;
                break;
            }
        }

        // Check if desired option is found
        if (desiredOption != null) {
            // Click on the desired option
            desiredOption.click();
        } else {
            System.out.println("Desired option not found!");
        }
    }
}

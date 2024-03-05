package pajeobjects.forms;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pajeobjects.BasePage;
import utils.ActionDriver;

import java.time.Duration;

public class TMobileTestForm extends BasePage {
    public TMobileTestForm(ActionDriver actionDriver, WebDriverWait wait, JavascriptExecutor js){
        super(actionDriver,wait,js);
    }

    By TabletsPageIsDisplayedLocator = By.xpath("//h1[contains(text(),'Tablets')]");
    public boolean TabletsPageIsDisplayed() {
        return actionDriver.elementIsVisible(TabletsPageIsDisplayedLocator, 5);
    }

    public void selectFilter(String menu, String... subMenu) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//legend[normalize-space()='" + menu + "']")));
        menuElement.click();

        if (subMenu[0].equals("all")) {
            String[] allFilters = {};
            switch (menu) {
                case "Deals":
                    allFilters = new String[]{"New", "Special offer"};
                    break;
                case "Brands":
                    allFilters = new String[]{"Alcatel", "Apple", "Samsung", "T-Mobile", "TCL"};
                    break;
                case "Operating System":
                    allFilters = new String[]{"Android", "iPadOS", "Other"};
                    break;
            }

            for (String filter : allFilters) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + filter + "')]/ancestor::label/span[1]")));
                element.click();
            }

        } else {
            for (String name : subMenu) {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + name + "')]/ancestor::label/span[1]")));
                element.click();
            }
        }
    }
}

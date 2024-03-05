package pajeobjects.forms;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
    public void navigateToMenu(String menuText){
        Actions action = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-analytics-navigation-name='"+menuText+"']")));
        action.moveToElement(menu).build().perform();
    }
    public void navigateToSubMenu(String subMenuText){
        Actions action = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement submenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@data-analytics-navigation-name='"+subMenuText+"']")));
        action.moveToElement(submenu).click().build().perform();
    }
    //--//
    //visibilityOfElementLocated - InProgress
    //--//
    public void selectFilter(String menu, String... subMenu) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//legend[contains(text(),'" + menu + "')]")));
        // WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//legend[normalize-space()='" + menu + "']")));
        menuElement.click();

        if (subMenu[0].equals("all")) {
            String[] allFilters = getFiltersForMenu(menu);
            for (String filter : allFilters) {
                clickFilter(filter, wait);
            }
        } else {
            for (String name : subMenu) {
                clickFilter(name, wait);
            }
        }
    }
    private String[] getFiltersForMenu(String menu) {
        switch (menu) {
            case "Deals":
                return new String[]{"New", "Special offer"};
            case "Brands":
                return new String[]{"Alcatel", "Apple", "Samsung", "T-Mobile", "TCL"};
            case "Operating System":
                return new String[]{"Android", "iPadOS", "Other"};
            default:
                return new String[]{};
        }
    }
    private void clickFilter(String filter, WebDriverWait wait) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(), '" + filter + "')]/ancestor::label/span[1]")));
        element.click();
    }
}

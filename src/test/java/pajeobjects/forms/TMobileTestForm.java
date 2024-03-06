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
    public void selectFilter(String menu, String... subMenu) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement menuElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//legend[contains(text(),'"+menu+"')]")));
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
        By filterLocator = getLocatorForFilter(filter);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(filterLocator));
        element.click();
    }

    private By getLocatorForFilter(String filter) {
        switch (filter) {
            case "New":
                return DealsNewLocator;
            case "Special offer":
                return DealsSpecialOfferLocator;
            case "Alcatel":
                return BrandsAlcatelLocator;
            case "Apple":
                return BrandsAppleLocator;
            case "Samsung":
                return BrandsSamsungLocator;
            case "T-Mobile":
                return BrandsTMobileLocator;
            case "TCL":
                return BrandsTCLLocator;
            case "Android":
                return OperationSystemAndroidLocator;
            case "iPadOS":
                return OperationSystemiPadOSLocator;
            case "Other":
                return OperationSystemOtherLocator;
            default:
                throw new IllegalArgumentException("Unsupported filter: " + filter);
        }
    }

    By DealsNewLocator = By.id("mat-checkbox-1");
    By DealsSpecialOfferLocator = By.id("mat-checkbox-2");
    By BrandsAlcatelLocator = By.id("mat-checkbox-3");
    By BrandsAppleLocator = By.id("mat-checkbox-4");
    By BrandsSamsungLocator = By.id("mat-checkbox-5");
    By BrandsTMobileLocator = By.id("mat-checkbox-6");
    By BrandsTCLLocator = By.id("mat-checkbox-7");
    By OperationSystemAndroidLocator = By.id("mat-checkbox-8");
    By OperationSystemiPadOSLocator = By.id("mat-checkbox-9");
    By OperationSystemOtherLocator = By.id("mat-checkbox-10");
}

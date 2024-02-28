package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import utils.logger.ActionLogger;
import utils.logger.Colors;
import utils.logger.Highlighter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ActionDriver {
    private final ActionLogger actionLogger;
    private final Highlighter highlighter;
    private final WebDriver driver;

    public ActionDriver(WebDriver driver, ActionLogger actionLogger) {
        this.driver = driver;
        this.actionLogger = actionLogger;
        this.highlighter = new Highlighter(actionLogger, (JavascriptExecutor) driver, getWebDriverWait(3), true);
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public ActionLogger getActionLogger() {
        return actionLogger;
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public void staticWaitMilliSeconds(int milliSeconds) {
        actionLogger.logWaitMS(milliSeconds);
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void staticWaitSeconds(int seconds) {
        actionLogger.logWaitS(seconds);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void refresh(WebDriver driver) {
        driver.navigate().refresh();
    }

    public void scrollToTheBottom(JavascriptExecutor js, String... context) {
        actionLogger.logMethod(new String[]{ActionLogger.DEBUG});
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    //If a user needs to scroll up, they just need to modify the pixel value of the second parameter (for example 350) to a negative value (-350).
    public void scrollByPixels(JavascriptExecutor js, String pixels, String... context) {
        actionLogger.logMethod(new String[]{ActionLogger.DEBUG});
        js.executeScript("window.scrollBy(0,'" + pixels + "')", "");
    }

    public void setImplicitWait(int seconds, String... context) {
        actionLogger.logMethod(new String[]{ActionLogger.DEBUG});
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public WebDriverWait getWebDriverWait(int webDriverWaitDuration, String... context) {
        return getWebDriverWait(driver, webDriverWaitDuration);
    }

    public static WebDriverWait getWebDriverWait(WebDriver driver, int webDriverWaitDuration) {
        return new WebDriverWait(driver, Duration.ofSeconds(webDriverWaitDuration));
    }

    public List<WebElement> getWebElementsList(By locator, String... context) {
        actionLogger.logMethod(context);
        return driver.findElements(locator);
    }

    public WebElement getWebElement(By locator, String... context) {
        actionLogger.logMethod(context);
        return driver.findElement(locator);
    }

    public List<WebElement> getWebElementsListByPresence(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> getWebElementsListByPresence(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        return getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> getWebElementsListByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> getWebElementsListByVisibility(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        return getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public int getWebElementsCountByWebDriver(By locator, String... context) {
        actionLogger.logMethod(context);
        return getWebElementsList(locator, ActionLogger.SKIP_LOGS).size();
    }

    public int getWebElementsCountByPresence(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementsListByPresence(wait, locator, ActionLogger.SKIP_LOGS).size();
        } catch (TimeoutException e) {
            return 0;
        }
    }

    public int getWebElementsCountByPresence(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementsListByPresence(locator, webDriverWaitDuration, ActionLogger.SKIP_LOGS).size();
        } catch (TimeoutException e) {
            return 0;
        }
    }

    public int getWebElementsCountByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementsListByVisibility(wait, locator, ActionLogger.SKIP_LOGS).size();
        } catch (TimeoutException e) {
            return 0;
        }
    }

    public int getWebElementsCountByVisibility(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
//        return getWebElementsListByVisibility( locator, webDriverWaitDuration).size();
        try {
            return getWebElementsListByVisibility(locator, webDriverWaitDuration, ActionLogger.SKIP_LOGS).size();
        } catch (TimeoutException e) {
            return 0;
        }
    }

    public WebElement getWebElementByIndex(By locator, int index, String... context) {
        actionLogger.logMethod(context);
        return getWebElementsList(locator, ActionLogger.SKIP_LOGS).get(index);
    }

    public Boolean elementIsPresent(By locator, String... context) {
        actionLogger.logMethod(context);
        return getWebElementsList(locator, ActionLogger.SKIP_LOGS).size() > 0;
    }

    public Boolean elementIsPresent(By locator, int waitDuration, String... context) {
        actionLogger.logMethod(context);
        return getWebElementsListByPresence(locator, waitDuration, ActionLogger.SKIP_LOGS).size() > 0;
    }

    public boolean elementIsVisible(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean elementIsVisible(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementByVisibility(getWebDriverWait(webDriverWaitDuration), locator, ActionLogger.SKIP_LOGS).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    //waits until an element is either invisible or not present on the DOM and returns boolean
    public boolean waitUntilInvisibilityOfElement(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    //waits until an element is either invisible or not present on the DOM and returns boolean
    public boolean waitUntilInvisibilityOfElement(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    //An expectation for checking that an element is either invisible or not present on the DOM.
    public void waitInvisibilityOfElementLocated(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    //An expectation for checking that an element is either invisible or not present on the DOM.
    public void waitInvisibilityOfElementLocated(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    //this method is used to select options by different wait strategies
    public void selectWebElementByValue(List<WebElement> webElements, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        boolean found = false;
        for (WebElement element : webElements) {
            System.out.println(element.getText());
            if (element.getText().trim().equals(value)) {
                found = true;
                element.click();
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("There is not element with text: " + value + " in given List");
        }
    }

    public void selectWebElementByContains(List<WebElement> webElements, String containsValue, String... context) {
        actionLogger.logMethod(context, "contains value: " + containsValue);
        for (WebElement element : webElements) {
            if (element.getText().trim().contains(containsValue)) {
                element.click();
                break;
            }
        }
    }

    public void selectOptionByValueWebDriver(By optionsLocator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        List<WebElement> options = getWebElementsList(optionsLocator, ActionLogger.SKIP_LOGS);
        selectWebElementByValue(options, value, ActionLogger.SKIP_LOGS);
    }

    public void selectOptionByValuePresence(WebDriverWait wait, By optionsLocator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        List<WebElement> options = getWebElementsListByPresence(wait, optionsLocator, ActionLogger.SKIP_LOGS);
        selectWebElementByValue(options, value, ActionLogger.SKIP_LOGS);
    }

    public void selectOptionByContainsValuePresence(WebDriverWait wait, By optionsLocator, String value, String... context) {
        actionLogger.logMethod(context, "contains value: " + value);
        List<WebElement> options = getWebElementsListByPresence(wait, optionsLocator, ActionLogger.SKIP_LOGS);
        selectWebElementByContains(options, value, ActionLogger.SKIP_LOGS);
    }

    public void selectOptionByValueVisibility(WebDriverWait wait, By optionsLocator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        List<WebElement> options = getWebElementsListByVisibility(wait, optionsLocator, ActionLogger.SKIP_LOGS);
        selectWebElementByValue(options, value, ActionLogger.SKIP_LOGS);
    }

    public void selectOptionByValueUsingActionsClassPresence(WebDriverWait wait, By optionsLocator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        List<WebElement> options = getWebElementsListByPresence(wait, optionsLocator, ActionLogger.SKIP_LOGS);
        for (WebElement option : options) {
//            System.out.println(option.getText().trim());
            if (option.getText().trim().equals(value)) {
                Actions actions = new Actions(driver);
                actions.moveToElement(option).doubleClick().build().perform();
                break;
            }
        }
    }

    public void selectOptionByIndexPresence(WebDriverWait wait, By optionsLocator, int index, String... context) {
        actionLogger.logMethod(context, "index: " + index);
        List<WebElement> options = getWebElementsListByPresence(wait, optionsLocator, ActionLogger.SKIP_LOGS);
        options.get(index - 1).click();
    }

    public void selectOptionByIndexVisibility(WebDriverWait wait, By locator, int index, String... context) {
        actionLogger.logMethod(context, "index: " + index);
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        options.get(index - 1).click();
    }

    public void selectOptionByIndexUsingActionsClassPresence(WebDriverWait wait, By optionsLocator, int index, String... context) {
        actionLogger.logMethod(context, "index: " + index);
        List<WebElement> options = getWebElementsListByPresence(wait, optionsLocator, ActionLogger.SKIP_LOGS);
        new Actions(driver).moveToElement(options.get(index - 1)).doubleClick().build().perform();
    }

    /*This method checks if the given text (passed as a parameter to the method) is present in the WebElement that matches the given web locator.
    It returns the Boolean value true if the specified text is present in the element; else, it returns false.
     */
    public boolean textToBePresentInElement(WebDriverWait wait, By elementLocator, String textValue, String... context) {
        actionLogger.logMethod(context, "text: " + textValue);
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(elementLocator, textValue));
    }

    public WebElement getWebElementByPresence(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement getWebElementByPresence(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        return getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement getWebElementByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement getWebElementByVisibility(By locator, int duration, String... context) {
        actionLogger.logMethod(context);
        return getWebDriverWait(duration).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement getWebElementByToBeClickable(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement getNestedWebElementByPresence(WebDriverWait wait, WebElement parent, By childLocator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, childLocator));
    }

    public List<WebElement> getNestedWebElementsByVisibility(WebDriverWait wait, WebElement parent, By childLocator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(parent, childLocator));
    }
    public List<WebElement> getNestedWebElementsByVisibility(WebDriverWait wait, By parentLocator, By childLocator, String... context) {
        actionLogger.logMethod(context);
        return wait.until(new ExpectedCondition<List<WebElement>>() {
            public List<WebElement> apply(WebDriver webDriver) {
                WebElement parent = driver.findElement(parentLocator);
                List<WebElement> allChildren = parent.findElements(childLocator);
                for (var child : allChildren) {
                    if (child.isDisplayed()) return allChildren;
                }
                return null;
            }

            public String toString() {
                return String.format("visibility of element located by %s -> %s", parentLocator, childLocator);
            }
        });
    }

    public List<WebElement> getPresentNestedWebElements(By parentLocator, By childLocator, String... context) {
        actionLogger.logMethod(context);
        try {
            return driver.findElement(parentLocator).findElements(childLocator);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public WebElement getWebElementByToBeClickable(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        return getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public String getTextByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        String text = getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).getText().trim();
        actionLogger.info("text: \"" + text + "\"");
        return text;
    }

    public void explicitClickByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).click();
    }

    public void explicitClickByElementToBeClickable(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        getWebElementByToBeClickable(wait, locator, ActionLogger.SKIP_LOGS).click();
    }

    public void javascriptClickByPresence(JavascriptExecutor js, WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        js.executeScript("arguments[0].click();", getWebElementByPresence(wait, locator, ActionLogger.SKIP_LOGS));
    }

    public void javascriptClickByVisibility(JavascriptExecutor js, WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        js.executeScript("arguments[0].click();", getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS));
    }

    public void javascriptClickByVisibility(JavascriptExecutor js, WebElement element, String... context) {
        actionLogger.logMethod(context);
        js.executeScript("arguments[0].click();", element);
    }

    public void actionsClickByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        new Actions(driver).moveToElement(getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS)).click().build().perform();
    }

    public void doubleClickByVisibility(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        new Actions(driver).doubleClick(getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS)).build().perform();
    }

    public void clickWebElement(WebElement element, String... context) {
        actionLogger.logMethod(context);
        highlighter.highlight(element, Colors.LIGHT_GREEN);
        element.click();
    }

    public void clickButtonInPersistentOverlay(WebDriverWait wait, By overlayLocator, By locator, String... context) {
        getWebElementByPresence(wait, overlayLocator).findElement(locator).click();
    }


    public void sendKeysByPresence(WebDriverWait wait, By locator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        getWebElementByPresence(wait, locator, ActionLogger.SKIP_LOGS).sendKeys(value);
    }

    public void sendKeysByVisibility(WebDriverWait wait, By locator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).sendKeys(value);
    }

    public void sendKeys(WebElement webElement, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        webElement.sendKeys(value);
    }

    public void sendKeysOneByOne(WebDriverWait wait, By locator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        WebElement input = getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS);
        for (char ch : value.toCharArray()) {
            input.sendKeys("" + ch);
        }
    }

    public void usualClear(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).clear();
    }

    //clear input using keys "CTRL a" + "DELETE"
    public void clearInputByCTRLA(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        clearInputByCTRLA(getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS), ActionLogger.SKIP_LOGS);
    }

    public void clearInputByCTRLA(WebElement input, String... context) {
        actionLogger.logMethod(context);
        String s = Keys.chord(Keys.CONTROL, "a");
        input.sendKeys(s);
        input.sendKeys(Keys.DELETE);
    }

    public void hoverOverWebElement(WebDriverWait wait, By locatorToHover, String... context) {
        actionLogger.logMethod(context);
        new Actions(driver).moveToElement(getWebElementByVisibility(wait, locatorToHover, ActionLogger.SKIP_LOGS)).build().perform();
    }

    public void mouseMoveAndClick(int xCoordinate, int yCoordinate, String... context) {
        actionLogger.logMethod(context);
        new Actions(driver).moveByOffset(xCoordinate, yCoordinate).click().build().perform();
    }

    public void hoverOverWebElementByValue(WebDriverWait wait, By optionsLocator, String value, String... context) {
        actionLogger.logMethod(context, "value: " + value);
        List<WebElement> options = getWebElementsListByVisibility(wait, optionsLocator);

        for (WebElement option : options) {
            if (option.getText().trim().equals(value)) {
                new Actions(driver).moveToElement(option).build().perform();
            }
        }
    }

    public String getAttributeValueByVisibility(WebDriverWait wait, By locator, String attributeName, String... context) {
        actionLogger.logMethod(context, "attribute name: " + attributeName);
        return getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).getAttribute(attributeName);
    }

    public String getCssValue(WebDriverWait wait, By locator, String propertyName) {
        actionLogger.logMethod(new String[]{ActionLogger.DEBUG});
        return getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).getCssValue(propertyName);
    }

    public void waitForAttributeToBe(WebDriverWait wait, By locator, String attributeName, String attributeValue) {
        wait.until(ExpectedConditions.attributeToBe(locator, attributeName, attributeValue));
    }

    public boolean waitForAttributeNotEmpty(WebElement element, String attribute, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context, "attribute name: " + attribute);
        return getWebDriverWait(webDriverWaitDuration).until(ExpectedConditions.attributeToBeNotEmpty(element, attribute));
    }

    public boolean waitUntilAttributeContains(WebDriverWait wait, By locator, String attribute, String value, String... context) {
        actionLogger.logMethod(context, "attribute name: " + attribute, "attribute value: " + value);
        try {
            wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean attributeIsPresent(WebDriverWait wait, By locator, String attributeName, String... context) {
        actionLogger.logMethod(context, "attribute name: " + attributeName);
        return Boolean.parseBoolean(getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).getAttribute(attributeName));
    }

    public int closeChildWindowsAndSwitchToParentWindow(int seconds) {
        actionLogger.logMethod(new String[]{ActionLogger.DEBUG});
        staticWaitSeconds(seconds);
        Set<String> windows = driver.getWindowHandles();

        //create iterator of strings and close all child windows
        Iterator<String> iterator = windows.iterator();

        String parentWindow = iterator.next();
        while (iterator.hasNext()) {
            driver.switchTo().window(iterator.next());
            driver.close();
        }

        //switch driver to parent window
        driver.switchTo().window(parentWindow);
        return windows.size();
    }

    //-----------------------------------------------------------soft assertion------------------------------------------------------------------------
    /* These methods raises an asserts but does not throw an exception since it is a Soft Assert */
    public void softAssertEquals(SoftAssert softAssert, String actualValue, String expectedValue, String message) {
        softAssert.assertEquals(actualValue, expectedValue, message);
    }

    public void softAssertEquals(SoftAssert softAssert, int actualValue, int expectedValue, String message) {
        softAssert.assertEquals(actualValue, expectedValue, message);
    }

    public void softAssertNotEquals(SoftAssert softAssert, String actualValue, String expectedValue, String message) {
        softAssert.assertNotEquals(actualValue, expectedValue, message);
    }

    public void softAssertTrue(SoftAssert softAssert, boolean booleanCondition, String message) {
        softAssert.assertTrue(booleanCondition, message);
    }

    public void softAssertFalse(SoftAssert softAssert, boolean booleanCondition, String message) {
        softAssert.assertFalse(booleanCondition, message);
    }

    public void softAssertNull(SoftAssert softAssert, Object object, String message) {
        softAssert.assertNull(object, message);
    }

    public void assertSame(SoftAssert softAssert, Object actual, Object expected, String message) {
        softAssert.assertSame(actual, expected, message);
    }

    //In the end, we have to call the assertAll() method that is used to throw the exceptions and results at the end of the test.
    public void assertAll(SoftAssert softAssert) {
        softAssert.assertAll();
    }

    public int getRandomNumber(int interval) {
        return new Random().nextInt(interval);
    }

    public List<String> getTextListByPresence(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        List<String> elementsTexts = new ArrayList<>();
        for (WebElement element : getWebElementsListByPresence(wait, locator, ActionLogger.SKIP_LOGS)) {
            elementsTexts.add(element.getText().trim());
        }
        return elementsTexts;
    }

    //print list of objects
    public static <T> void printObjectsList(List<T> objectsList) {
        for (Object element : objectsList) {
            System.out.println(element);
        }
    }

    public boolean waitUntilElementIsDisplayedExplicitly(WebDriverWait wait, By locator, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementByVisibility(wait, locator, ActionLogger.SKIP_LOGS).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean waitUntilElementIsDisplayedExplicitly(By locator, int webDriverWaitDuration, String... context) {
        actionLogger.logMethod(context);
        try {
            return getWebElementByVisibility(locator, webDriverWaitDuration, ActionLogger.SKIP_LOGS).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }


    //when comparing generic lists and there is not equals method overridden in list object
    public <T> void equals(List<T> list1, List<T> list2, String message) {
        if (list1.size() == list2.size()) {
            int count = list1.size();
            for (int i = 0; i < count; i++) {
                Assert.assertEquals(list2.get(i).toString(), list1.get(i).toString(), message);
            }
        } else {
            Assert.fail("ArrayLists sizes are not equal");
        }
    }

    public <T> boolean equals(List<T> list1, List<T> list2) {
        return list1.equals(list2);
    }

    //-------------------------------------------Table-------------------------------------------------------------------------------

    /**
     * An interface for defining click actions on objects of type T.
     */
    public interface ClickAction<T> {
        void perform(T object);
    }

    /**
     * An interface for extracting a comparable variable from an object of type T.
     */
    public interface ComparableVariable<T> {
        String getComparableVariable(T object);
    }

    /**
     * Clicks on a suitable row within a table based on a provided comparable variable and performs an action.
     */
    public <T> void clickSuitableRow(List<T> tableDTOS, ComparableVariable<T> getComparableVariable, String comparableVariable, ClickAction<T> action) {
        for (T tableObject : tableDTOS) {
            String comparableVariable1 = getComparableVariable.getComparableVariable(tableObject);
            if (comparableVariable1.contains(comparableVariable)) {
                action.perform(tableObject);
                return;
            }
        }
        throw new RuntimeException("could not find suitable row, with " + comparableVariable);
    }

    /**
     * Verifies the order of a table column based on a provided comparable variable.
     */
    public <T> boolean tableColumnOrderVerification(List<T> tableDTOS, ComparableVariable<T> getComparableVariable, boolean ascending) {
        for (int i = 1; i < tableDTOS.size(); i++) {
            String current = getComparableVariable.getComparableVariable(tableDTOS.get(i));
            String previous = getComparableVariable.getComparableVariable(tableDTOS.get(i - 1));
            int comparison = ascending ? previous.compareTo(current) : current.compareTo(previous);
            if (comparison > 0) {
                actionLogger.error("comparison failed: " + (i - 1) + "th = " + previous + "\nand " + i + "th = " + current);
                return false;
            }
        }
        return true;
    }

    public static <T extends Comparable<T>> boolean tableColumnOrderVerification1(List<T> tableValues, boolean ascending) {
        for (int i = 1; i < tableValues.size(); i++) {
            T current = tableValues.get(i);
            T previous = tableValues.get(i - 1);
            int comparison = ascending ? previous.compareTo(current) : current.compareTo(previous);
            if (comparison > 0) {
                System.out.println("Comparison failed: Element at index " + (i - 1) + " = " + previous +
                        "\nand Element at index " + i + " = " + current);
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the list of objects is sorted by date with hours in the specified order.
     */
    public <T> boolean isSortedDatesWithHours(List<T> tableDTOS, ComparableVariable<T> getComparableVariable, boolean ascending) {
        List<LocalDateTime> dates = tableDTOS.stream()
                .map(item -> LocalDateTime.parse(getComparableVariable.getComparableVariable(item), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .collect(Collectors.toList());

        for (int i = 1; i < dates.size(); i++) {
            LocalDateTime current = dates.get(i);
            LocalDateTime previous = dates.get(i - 1);
            int comparison = ascending ? previous.compareTo(current) : current.compareTo(previous);
            if (comparison > 0) {
                actionLogger.error("Comparison failed: " + (i - 1) + "th = " + previous + "\nand " + i + "th = " + current);
                return false;
            }
        }

        return true;
    }

    /**
     * A marker interface for defining conditions to be used in various operations.
     */
    public interface Condition<T> {
        boolean matches(T row);

    }

    /**
     * Verifies if the table contains a row that matches the specified conditions.
     */
    @SafeVarargs
    public final <T> boolean verifyTableContainsRow(List<T> table, Condition<T>... conditions) {
        System.out.println("table size = " + table.size());
        for (T t : table) {
            System.out.println(t);
        }
        for (T row : table) {
            boolean found = true;
            for (Condition<T> condition : conditions) {
                if (!condition.matches(row)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifies if the table or list of objects contains a cell that matches the specified conditions.
     */
    @SafeVarargs
    public final <T> boolean verifyTableContainsCell(List<T> table, Condition<T>... conditions) {
        for (T row : table) {
            boolean validRow = false;
            for (Condition<T> condition : conditions) {
                if (condition.matches(row)) {
                    validRow = true;
                    break;
                }
            }
            if (!validRow) return false;
        }
        return true;
    }

    /**
     * Asserts that all elements in a data array are equal to the expected value.
     */
    public static <T> void isDataArrayEqual(List<T> dataArray, String expectedValue) {
        for (T t : dataArray) {
            Assert.assertEquals(t.toString(), expectedValue, "" + t + " are not equal to the " + expectedValue + "");
        }
    }

    public String captureScreenshot(WebDriver driver) {
        return captureScreenshot("" + System.currentTimeMillis());
    }

    /**
     * takes screenshot in driver and saves it with given name in target/screenshots dir
     *
     * @param screenshotName screenshot name
     * @return file path to the screenshot
     */
    public String captureScreenshot(String screenshotName) {
        String screenshotDir = "target/screenshots/";
        try {
            File logDirectory = new File(screenshotDir);
            if (!logDirectory.exists()) {
                boolean created = logDirectory.mkdirs(); // Create the directory and check the result
                if (!created) {
                    actionLogger.error("Creating the directory: " + screenshotDir + " Failed");
                }
            }

            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            String filePath = screenshotDir + screenshotName + ".png";
            File destinationFile = new File(filePath);
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot captured: " + destinationFile.getAbsolutePath());
            return filePath;
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
        return null;
    }

    /**
     * Clicks on the specified element repeatedly until it disappears or the maximum attempts are reached.
     *
     * @param locator             The By object representing the element to be clicked.
     * @param waitDuration        The maximum duration (in seconds) to wait for the element to be visible.
     * @param maxAttempts         The maximum number of attempts to click the element.
     * @param retryIntervalMillis The time (in milliseconds) to wait between attempts.
     */
    public void clickUntilDisappear(By locator, int waitDuration, int maxAttempts, int retryIntervalMillis) {
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            System.out.println("attempt: " + attempt);
            try {
                if (elementIsVisible(locator, waitDuration)) {
                    getWebElementByToBeClickable(locator, waitDuration).click();
                    // You may add a delay between attempts if needed
                    staticWaitMilliSeconds(retryIntervalMillis);
                } else {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Attempts to click the element identified by the given locator, with retries.
     *
     * @param wait                   The WebDriverWait instance.
     * @param locator                The By locator of the element to be clicked.
     * @param maxClickAttempts       Maximum number of attempts to click the element.
     * @param clickRetryDelaySeconds Delay between click attempts (in seconds).
     * @throws RuntimeException if the click operation is unsuccessful after the maximum attempts.
     */
    public void clickWithRetry(
            WebDriverWait wait,
            By locator,
            int maxClickAttempts,
            int clickRetryDelaySeconds
    ) {
        for (int attempt = 1; attempt <= maxClickAttempts; attempt++) {
            try {
                explicitClickByElementToBeClickable(wait, locator);
                // If click succeeds, exit the loop
                return;
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + ": Click failed. Retrying...");
            }

            // Delay before the next attempt
            staticWaitSeconds(clickRetryDelaySeconds);
        }

        // If the loop completes without a successful click, throw an exception
        throw new RuntimeException("Max click attempts reached. Click operation unsuccessful.");
    }

    /**
     * Validates the visibility status of multiple elements based on the specified visibility condition.
     * <p>
     * This method is designed to assert the visibility status of a variable number of elements
     * according to the specified visibility condition. If the visibility status is set to true,
     * it asserts that all elements are true (visible). If the visibility status is set to false,
     * it asserts that all elements are false (not visible).
     *
     * @param visibilityStatus The desired visibility condition. If true, expects all elements to be visible.
     *                         If false, expects all elements to be not visible.
     * @param elements         A variable number of boolean values representing the visibility status
     *                         of individual elements.
     * @throws AssertionError If the visibility status of any element does not match the expected condition.
     *                        The assertion error will include details about which element(s) failed the assertion.
     * @see Assert#assertTrue(boolean)
     * @see Assert#assertFalse(boolean)
     */
    public void elementsVisibility(boolean visibilityStatus, boolean... elements) {
        if (visibilityStatus) {
            for (boolean elementStatus : elements) {
                Assert.assertTrue(elementStatus);
            }
        } else {
            for (boolean elementStatus : elements) {
                Assert.assertFalse(elementStatus);
            }
        }
    }

    public void capturePageSource(String fileName) {
        String targetDirectory = "target/page-sources";
        String pageSource = driver.getPageSource();

        File directory = new File(targetDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = targetDirectory + File.separator + fileName + ".html";

        try {
            FileUtils.writeStringToFile(new File(filePath), pageSource, "UTF-8");

            System.out.println("Page source has been successfully written to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write page source to file: " + e.getMessage());
        }
    }

    public String getElementValueByJs(JavascriptExecutor js, String cssLocator) {
        return (String) js.executeScript("return document.querySelector('" + cssLocator + "').value");
    }

    public String getElementValueByIdJs(JavascriptExecutor js, String id) {
        return (String) js.executeScript("return document.getElementById('" + id + "').value");
    }

    public void clickUntilAttributeContains(WebDriverWait wait, By locator, String attribute, String value) {
        for (int i = 0; i < 10; i++) {
            WebElement element = getWebElementByVisibility(wait, locator);

            if (!element.getAttribute(attribute).contains(value)) {
                explicitClickByVisibility(wait, locator);
            } else {
                break;
            }
        }
    }

    public void clickUntilElementIsDisplayed(WebDriverWait wait, By locator, By clickableElementLocator) {
        waitInvisibilityOfElementLocated(locator, 2);

        for (int i = 0; i < 10; i++) {
            boolean elementIsDisplayed = elementIsVisible(locator, 1);
            System.out.println(elementIsDisplayed);
            if (!elementIsDisplayed) {
                getWebElementByVisibility(wait, clickableElementLocator).click();
            } else {
                break;
            }
        }
    }
}

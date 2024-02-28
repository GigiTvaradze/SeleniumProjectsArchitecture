package utils.logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Highlighter implements Colors {
    private static final String CLICK_COLOR = LIGHT_GREEN;  //color when the element is clicked
    private static final String VISIBILITY_COLOR = LIGHT_BLUE; //color when the element is seen
    private static final String INPUT_COLOR = LIGHT_YELLOW; //color when we type into the element
    private static final String GET_TEXT_COLOR = LIGHT_PINK; //color when we extract text from the element

    private final ActionLogger actionLogger;
    private final JavascriptExecutor js;
    private boolean enabled;
    private WebDriverWait wait;

    public Highlighter(ActionLogger actionLogger, JavascriptExecutor js, WebDriverWait wait, boolean enabled) {
        this.actionLogger = actionLogger;
        this.js = js;
        this.enabled = enabled;
        this.wait = wait;
    }

    public Highlighter(ActionLogger actionLogger, JavascriptExecutor js, WebDriverWait wait) {
        this.actionLogger = actionLogger;
        this.js = js;
        this.enabled = true;
        this.wait = wait;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public void click(By locator) {
        if (!enabled) return;
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            click(element);
        } catch (Exception e) {
            actionLogger.debug("could not highlight the element By Locator: " + locator);
        }
    }

    public void click(WebElement element) {
        if (!enabled) return;
        highlight(element, CLICK_COLOR);
    }

    public void visibility(By locator) {
        if (!enabled) return;
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            visibility(element);
        } catch (Exception e) {
            actionLogger.debug("could not highlight the element By Locator: " + locator);
        }
    }

    public void visibility(WebElement element) {
        if (!enabled) return;
        highlight(element, VISIBILITY_COLOR);
    }

    public void input(By locator) {
        if (!enabled) return;
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            input(element);
        } catch (Exception e) {
            actionLogger.debug("could not highlight the element By Locator: " + locator);
        }
    }

    public void input(WebElement element) {
        if (!enabled) return;
        highlight(element, INPUT_COLOR);
    }

    public void getText(By locator) {
        if (!enabled) return;
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            getText(element);
        } catch (Exception e) {
            actionLogger.debug("could not highlight the element By Locator: " + locator);
        }
    }

    public void getText(WebElement element) {
        if (!enabled) return;
        highlight(element, GET_TEXT_COLOR);
    }

    public void highlight(By locator) {
        if (!enabled) return;
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            highlight(element);
        } catch (Exception e) {
            actionLogger.debug("could not highlight the element By Locator: " + locator);
        }
    }

    public void highlight(WebElement element) {
        if (!enabled) return;
        highlight(element, null);
    }

    public void highlight(WebElement element, String backgroundColor) {
        if (!enabled) return;
        try {
            js.executeScript("arguments[0].style.border = '2px solid red';", element);
            if (backgroundColor != null) {
                js.executeScript("arguments[0].style.backgroundColor = '" + backgroundColor + "';", element);
                System.out.println(backgroundColor);
            } else {
                js.executeScript("arguments[0].style.filter = 'brightness(80%)';", element);
            }
        } catch (Exception e) {
            actionLogger.debug("couldn't highlight the element.\n" + e.getMessage());
        }
    }

    //static methods ===================================================================================================
    public static void highlight(JavascriptExecutor js, WebDriverWait wait, By locator) {
        highlight(js, wait.until(ExpectedConditions.visibilityOfElementLocated(locator)), null);
    }

    public static void highlight(JavascriptExecutor js, WebElement element) {
        highlight(js, element, null);
    }

    public static void highlight(JavascriptExecutor js, WebElement element, String backgroundColor) {
        try {
            js.executeScript("arguments[0].style.border = '2px solid red';", element);
            if (backgroundColor != null) {
                js.executeScript("arguments[0].style.backgroundColor = '" + backgroundColor + "';", element);
            } else {
                js.executeScript("arguments[0].style.filter = 'brightness(80%)';", element);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
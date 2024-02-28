package pajeobjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ActionDriver;
import utils.logger.ActionLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor js;
    protected ActionDriver actionDriver;
    protected ActionLogger actionLogger;

    public BasePage() {

    }
    public BasePage(ActionDriver actionDriver) {
        this.actionDriver = actionDriver;
        this.actionLogger = actionDriver.getActionLogger();
        this.driver = actionDriver.getWebDriver();
    }

    public BasePage(ActionDriver actionDriver, WebDriverWait wait) {
        this.actionDriver = actionDriver;
        this.actionLogger = actionDriver.getActionLogger();
        this.driver = actionDriver.getWebDriver();
        this.wait = wait;
    }

    public BasePage(ActionDriver actionDriver, WebDriverWait wait, JavascriptExecutor js) {
        this.actionDriver = actionDriver;
        this.actionLogger = actionDriver.getActionLogger();
        this.driver = actionDriver.getWebDriver();
        this.wait = wait;
        this.js = js;
    }

    //protected final By Locator = By.xpath("");

    protected final static String SORT_STATE_ATTRIBUTE = "class";
    protected void sort(By sortButtonLocator, SortState state) {
        for (int i = 0; i < SortState.values().length; i++) {
            WebElement sortButton = actionDriver.getWebElementByToBeClickable(wait, sortButtonLocator, ActionLogger.SKIP_LOGS);
            String sortValue = sortButton.getAttribute(SORT_STATE_ATTRIBUTE);
            if (sortValue == null)
                throw new RuntimeException(String.format("sort state attribute '%s' in element located %s is null", SORT_STATE_ATTRIBUTE, sortButtonLocator.toString()));
            if (sortValue.contains(state.toString())) {
                return;
            }
            actionDriver.clickWebElement(sortButton, ActionLogger.DEPTHS_PREVIOUS);
        }
    }

    /* needs refactoring if sort state attribute(ng-reflect-name) values change */
    public enum SortState {
        //not sorted
        SORT("sort"),
        //increasing
        SORT_UP("sort up"),
        //decreasing
        SORT_DOWN("sort down");

        private final String attributeValue;

        SortState(String attributeValue) {
            this.attributeValue = attributeValue;
        }

        @Override
        public String toString() {
            return attributeValue;
        }
    }

    public enum SortColumnType {
        //not sorted
        STRING("String"),
        //increasing
        INTEGER("Integer"),
        //decreasing
        DATE("Date"),
        DOUBLE("Double");

        private final String attributeValue;

        SortColumnType(String attributeValue) {
            this.attributeValue = attributeValue;
        }

        @Override
        public String toString() {
            return attributeValue;
        }
    }
    //--------------------------------------------------------------------data table getter--------------------------------------------------------------------

    private static final boolean loggingPhases = false;
    private static final String loggingActionDriver = ActionLogger.SKIP_LOGS;
    private static final By tableRowsLocator = By.xpath("//bg-ui-table-row[contains(@id,'_table_row-')]");

    /**
     * Retrieves data from a dynamic web page table and converts it into a list of DTO objects.
     */
    public <T> List<T> getTableFromPage(Function<WebElement, T> createDtoFunction, By dynamicTableLocator) {
        WebElement dynamicTable = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicTableLocator));
        List<WebElement> tableRows = actionDriver.getNestedWebElementsByVisibility(wait, dynamicTable, By.xpath(".//bg-ui-table-row"), ActionLogger.TRACE);

        List<T> tableItems = new ArrayList<>();

        for (WebElement row : tableRows) {
            if (loggingPhases) actionDriver.getActionLogger().debug("NEXT ROW");
            T tableItem = createDtoFunction.apply(row);
            tableItems.add(tableItem);
        }
        if (loggingPhases) actionDriver.getActionLogger().debug("DONE GETTING TABLE, ROW COUNT: " + tableRows.size());
        return tableItems;
    }

    /**
     * Retrieves data from a dynamic web page table and converts it into a list of DTO objects.
     */
    public List<String> getTableColumnTexts(int columnIndex, By dynamicTableLocator) {
        WebElement dynamicTable = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicTableLocator));
        List<WebElement> tableRows = actionDriver.getNestedWebElementsByVisibility(wait, dynamicTable, By.xpath(".//bg-ui-table-row"), ActionLogger.TRACE);

        List<String> columnTexts = new ArrayList<>();

        for (WebElement row : tableRows) {
            columnTexts.add(getTableCellText(row, columnIndex));
        }
        return columnTexts;
    }

    /**
     * Retrieves the text content of a specific cell within a table row.
     */
    public String getTableCellText(WebElement row, int cellIndex) {
        if (loggingPhases) actionDriver.getActionLogger().debug("NEXT COLUMN");
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id, '_data-cell-%d')]", cellIndex);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver).getText().trim();
    }

    public String getTableCellText(WebElement row, int cellIndex, String tagName) {
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id,'_data-cell-%d')]/%s", cellIndex, tagName);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver).getText().trim();
    }

    /**
     * Retrieves the specific button of a specific cell within a table row.
     */
    public WebElement getTableCellButton(WebElement row, int cellIndex) {
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id, '_data-cell-%d')]//button", cellIndex);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    public WebElement getTableCellCheckbox(WebElement row, int cellIndex) {
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id, '-data-cell-%d')]//bg-ui-checkbox", cellIndex);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    public WebElement getTableCellSpan(WebElement row, int cellIndex) {
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id, '_data-cell-%d')]//span", cellIndex);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    public WebElement getTableCellButton1(WebElement row, int cellIndex, int buttonIndexInCell) {
        String cellLocator = String.format("(./bg-ui-table-data[contains(@id, 'data-cell-%d')]//bg-ui-button)[%d]", cellIndex, buttonIndexInCell);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    /**
     * Retrieves the specific WebElement of a specific cell within a table row.
     */
    public WebElement getTableCellWebElement(WebElement row, int cellIndex, String tagName) {
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id,'_data-cell-%d')]//%s", cellIndex, tagName);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), ActionLogger.TRACE);
    }

    public WebElement getTableCellWebElement(WebElement row, int cellIndex, String tagName, int elementIndex) {
        String cellLocator = String.format("./bg-ui-table-data[contains(@id,'_data-cell-%d')]//%s[%d]", cellIndex, tagName, elementIndex);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    /**
     * Retrieves the text content of a specific cell within a table row, using a dash in the cellLocator format.
     */
    public String getTableCellTextDash(WebElement row, int cellIndex) {
        if (loggingPhases) actionDriver.getActionLogger().debug("NEXT COLUMN");
        String cellLocator = String.format(".//bg-ui-table-data[contains(@id, '-data-cell-%d')]", cellIndex);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver).getText().trim();
    }

    /**
     * Retrieves a button element within a specific cell of a table row based on the button's name.
     */
    public WebElement getTableCellButton(WebElement row, String buttonName) {
        if (loggingPhases) actionDriver.getActionLogger().debug("NEXT COLUMN");
        String cellLocator = String.format("./bg-ui-table-data[contains(@id, '-data-cell-1')]//button[contains(@id, '%s')]", buttonName);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    public WebElement getTableCellBgUiButton(WebElement row, String buttonName) {
        if (loggingPhases) actionDriver.getActionLogger().debug("NEXT COLUMN");
        String cellLocator = String.format("./bg-ui-table-data[contains(@id, '-data-cell-1')]//bg-ui-button[contains(@id, '%s')]", buttonName);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    /**
     * Retrieves a button element within a specific cell of a table row based on the button's name.
     */
    public WebElement getTableCellButton(WebElement row, int cellIndex, String buttonName) {
        String cellLocator = String.format("./bg-ui-table-data[contains(@id, '_data-cell-%d')]//button[contains(@id, '%s')]", cellIndex, buttonName);
        return actionDriver.getNestedWebElementByPresence(wait, row, By.xpath(cellLocator), loggingActionDriver);
    }

    /**
     * Verifies if the actual table column headers extracted using the headerExtractor function
     * match the expected headers. The headerExtractor function is used to obtain the actual
     * headers based on an index and an optional offset (firstColumnIndex).
     */
    public <T> boolean verifyTableColumnHeaders(List<String> expectedHeaders, Function<Integer, String> headerExtractor, int firstColumnIndex) {
        for (int i = 0; i < expectedHeaders.size(); i++) {
            String actualHeader = headerExtractor.apply(i + firstColumnIndex);
            if (!expectedHeaders.get(i).equals(actualHeader)) {
                actionDriver.getActionLogger().error("expected: " + expectedHeaders.get(i) + ", actual: " + actualHeader);
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the header text of a table column located at the specified index.
     */
    public String getTableColumnHeaderTextByIndex(int cellIndex) {
        String cellLocator = String.format("//div[contains(@id,'_table_columns-0-%d-title')]", cellIndex);
        return actionDriver.getTextByVisibility(new BasePage().wait, By.xpath(cellLocator));
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
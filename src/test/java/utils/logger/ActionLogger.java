package utils.logger;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.Test;

public class ActionLogger {
    public static final String SKIP_LOGS = "SKIP_LOGS";
    public static final String HIDE_INPUT = "HIDDEN_INPUT";
    public static final String SKIP_PARAMETERS = "SKIP_PARAMETERS";
    public static final String TRACE = "TRACE";
    public static final String DEBUG = "DEBUG";
    public static final String INFO = "INFO"; //default
    public static final String ERROR = "ERROR";
    public static final String DEPTHS_CURRENT = "DEPTHS_CURRENT";
    public static final String DEPTHS_PREVIOUS = "DEPTHS_PREVIOUS";
    public static final String DEPTHS_PREVIOUS2 = "DEPTHS_PREVIOUS2";
    private final Logger logger;
    private final String pathToFile;

    /**
     * centralized class for main logging events
     *
     * @param logger log4j logger object
     */
    public ActionLogger(Logger logger, String pathToFile) {
        this.logger = logger;
        this.pathToFile = pathToFile;
    }

    public String getLoggerName() {
        return logger.getName();
    }

    public String getPathToFile() {
        return pathToFile;
    }

    //=================== GENERAL LOGS ===================
    public synchronized void trace(Object message) {
        checkFilePath();
        logger.trace(message);
    }

    public synchronized void debug(Object message) {
        checkFilePath();
        logger.debug(message);
    }

    public synchronized void info(Object message) {
        checkFilePath();
        logger.info(message);
    }

    public synchronized void warn(Object message) {
        checkFilePath();
        logger.warn(message);
    }

    public synchronized void error(Object message) {
        checkFilePath();
        logger.error(message);
    }

    public synchronized void fatal(Object message) {
        checkFilePath();
        logger.fatal(message);
    }

    private void checkFilePath() {
        MyLogManager.setUpFilePath(pathToFile);
    }

    //=================== TEST LOGS ===================
    public void startLogs(ITestResult result) {
        info("test starting: " + result.getMethod().getMethodName() + appendTestParameters(result.getParameters()));
    }

    private String appendTestParameters(Object[] parameters) {
        if (parameters.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i].toString());
            if (i != parameters.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public void endLogs(ITestResult result) {
        String testName = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).testName();
        switch (result.getStatus()) {
            case (ITestResult.SUCCESS):
                info("test: " + testName + " ended with success.");
                break;
            case (ITestResult.FAILURE):
                info("test " + testName + " ended with failure.");
                break;
            case (ITestResult.SKIP):
                info("test " + testName + " was skipped.");
                break;
            default:
                info("test " + testName + " ended with unknown results.");
                break;
        }
    }

    public void logStep(String jiraName, String jiraCode) {
        info("=================" + "STEP: " + jiraName + " JIRA: " + jiraCode + "=================");
    }

    public void logStep(String jiraName, String jiraCode, String JiraURL) {
        info("=================" + "STEP: " + jiraName + " JIRA: " + jiraCode + "URL: " + JiraURL + " =================");
    }

    public void logStep(int stepNum, String description) {
        info("================= " + "STEP: " + stepNum + " " + description + " =================");
    }

    public void logStep(String description) {
        info("================= " + "STEP: " + description + " =================");
    }

    public void logStep(int stepNum) {
        info("================= " + "STEP: " + stepNum + " =================");
    }

    public void logAssert(String description) {
        info("ASSERTING " + description + "...");
    }

    public void logAssert() {
        info("ASSERTING...");
    }

    //=================== DEV TOOLS MANAGER LOGS ===================
    private boolean networkLogged = false;

    public boolean isNetworkLogged() {
        return networkLogged;
    }

    public synchronized void logNetworkError(String url, int status, String responseBody, String requestBody) {
        networkLogged = true;
        error("{" +
                "\"url\":" + "\"" + url + "\"" + ","
                + "\"status\":" + status + ","
                + "\"payload\":" + requestBody + ","
                + "\"response\":" + responseBody +
                "}");
    }

    public synchronized void logNetworkResponse(String url, Integer status, String responseBody, String requestBody) {
        networkLogged = true;
        info("{" +
                "\"url\":" + "\"" + url + "\"" + ","
                + "\"status\":" + status + ","
                + "\"payload\":" + requestBody + ","
                + "\"response\":" + responseBody +
                "}");
    }

    //=================== ACTION DRIVER LOGS ===================
    public void logMethod(String[] methodContext, String... parameters) {
        if (methodContext.length != 0 && methodContext[0].equals(SKIP_LOGS)) return;

        //finding method outside ActionDriver classes to start logging
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        int startIndex = 3;
        for (int i = stack.length - 1; i > 0; i--) {
            if (stack[i].getFileName() != null && stack[i].getFileName().contains("ActionDriver")) {
                startIndex = i;
                break;
            }
        }

        if (arrContains(methodContext, DEPTHS_PREVIOUS)) {
            startIndex += 2;
        } else if (arrContains(methodContext, DEPTHS_PREVIOUS2)) {
            startIndex += 3;
        } else {
            startIndex += 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i > 1; i--) {
            sb.append(stack[i].getMethodName());
            if (i > 2) sb.append(" -> ");
        }

        if (parameters.length > 0 && (methodContext.length == 0 || !methodContext[0].equals(SKIP_PARAMETERS)))
            sb.append(" \n                        [").append(String.join(", ", parameters)).append("]");


        if (arrContains(methodContext, DEBUG)) {
            this.debug(sb);
            return;
        } else if (arrContains(methodContext, TRACE)) {
            this.trace(sb);
            return;
        } else if (arrContains(methodContext, ERROR)) {
            this.error(sb);
            return;
        }

        this.info(sb);
    }

    private static boolean arrContains(String[] arr, String str) {
        for (String element : arr) {
            if (element.equals(str)) return true;
        }
        return false;
    }

    // === ELEMENT INTERACTIONS ===
    public void logClick(String[] elementName) {
        if (elementName.length > 0) info("clicking: " + elementName[0]);
    }

    public void logClickListOption(String[] listName, String value) {
        if (listName.length > 0) info("clicking: \"" + value + "\" in " + listName[0]);
    }

    public void logDoubleClick(String[] elementName) {
        if (elementName.length > 0) info("double clicking: " + elementName[0]);
    }

    public void logHover(String[] elementName) {
        if (elementName.length > 0) info("hovering on " + elementName[0]);
    }

    public void logInput(String[] elementName, String value) {
        if (elementName.length > 0) {
            String val = value;
            if (elementName.length > 1 && elementName[1].equals(HIDE_INPUT)) {
                val = HIDE_INPUT;
            }
            info("inputting \"" + val + "\" in " + elementName[0]);
        }
    }

    public void logClear(String[] elementName) {
        if (elementName.length > 0) info("clearing " + elementName[0]);
    }

    // === GETTERS ===
    public void logGetElement(String[] elementName) {
        if (elementName.length > 0) info("getting element: " + elementName[0]);
    }

    public void logGetElementFromList(String[] listName) {
        if (listName.length > 0) info("getting element from list: " + listName[0]);
    }

    public void logGetList(String[] listName) {
        if (listName.length > 0) info("getting list: " + listName[0]);
    }

    public void logGetListCount(String[] listName) {
        if (listName.length > 0) info("counting items: " + listName[0]);
    }

    public void logGetText(String[] elementName) {
        if (elementName.length > 0) info("getting text from " + elementName[0]);
    }

    public void logGetAttribute(String[] elementName, String attribute) {
        if (elementName.length > 0) info("getting attribute \"" + attribute + "\" from " + elementName[0]);
    }

    // === WAITS ===
    public void logWaitForPresence(String[] elementName) {
        if (elementName.length > 0) info("waiting for presence of " + elementName[0]);
    }

    public void logWaitForInvisibility(String[] elementName) {
        if (elementName.length > 0) info("waiting for invisibility of " + elementName[0]);
    }

    public void logWaitForVisibility(String[] elementName) {
        if (elementName.length > 0) info("waiting for visibility of " + elementName[0]);
    }

    public void logWaitForTextPresence(String[] elementName, String textValue) {
        if (elementName.length > 0) info("waiting for text \"" + textValue + "\" in " + elementName[0]);
    }

    public void logWaitAttributeContains(String[] elementName, String attribute, String value) {
        if (elementName.length > 0)
            info("waiting for attribute \"" + attribute + " = '" + value + "'\" in " + elementName[0]);
    }

    public void logWaitAttributeNotEmpty(String[] elementName, String attribute) {
        if (elementName.length > 0)
            info("waiting for attribute \"" + attribute + "\" not to be empty in " + elementName[0]);
    }

    public void logWaitMS(int milliSeconds) {
        debug("static wait for " + milliSeconds + "ms");
    }

    public void logWaitS(int seconds) {
        debug("static wait for " + seconds + "s");
    }

    // === CHECKING CONDITIONS ===
    public void logCheckPresence(String[] elementName) {
        if (elementName.length > 0) info("checking if " + elementName[0] + " is present");
    }

    public void logCheckVisibility(String[] elementName) {
        if (elementName.length > 0 && logger != null) info("checking if " + elementName[0] + " is visible");
    }

    public void logCheckAttributePresence(String[] elementName, String attribute) {
        if (elementName.length > 0)
            info("checking presence of attribute \"" + attribute + "\" in " + elementName[0]);
    }
}


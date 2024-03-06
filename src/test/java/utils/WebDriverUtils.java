package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class WebDriverUtils {
    private final static String screenshotDir = "target/screenshots/";

    public static String captureScreenshot(WebDriver driver) {
        return captureScreenshot(driver, System.currentTimeMillis() + "");
    }

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            //make screenshotDir if it doesn't exist
            File logDirectory = new File(screenshotDir);
            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }

            //take screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            //copy screenshot and paste in screenshotDir
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
}

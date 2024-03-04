package pajeobjects.forms;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import pajeobjects.BasePage;
import utils.ActionDriver;

public class TmobileTestForm extends BasePage {
    public TmobileTestForm(ActionDriver actionDriver, WebDriverWait wait, JavascriptExecutor js){
        super(actionDriver,wait,js);
    }

    By TabletsPageIsDisplayedLocator = By.xpath("");

}

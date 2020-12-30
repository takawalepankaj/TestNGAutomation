package com.frame;
import com.lib.EnvSetup;
import com.lib.UtilityFun;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
/**
 * This is for Common Library function used by all POM Classes
 *
 * @version 0.1
 * Date :
 * @author :
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */
public class PageHelper extends UtilityFun {
    protected Logger log = Logger.getLogger(this.getClass().getName());
    // Member variables
    protected WebDriver driver;

    @FindBy(xpath = "//a[text()='Careers']")
    public WebElement LoginButton;

    public PageHelper(WebDriver dr) {
        driver = dr;

    }
    /***
     * This method will verify title and Message for given Web Controls.
     *
     * @param title          : Title of the message box.
     * @param message        : Message string.
     * @param titleElement   : Web element for the Title.
     * @param messageElement : Web element for the Message.
     * @return : True or False depending on the results
     */
    public boolean verifyMessageBox(String title, String message, WebElement titleElement, WebElement messageElement) {
        try {
            this.eXISTs(driver, titleElement, 30);
            if (!titleElement.getText().equals(title)) {
                log.info("Expected Alert [" + title + "] not shown... Actual Title is [" + titleElement.getText() + "]");
                return false;
            }
            if (!messageElement.getText().equals(message)) {
                if (!messageElement.getText().contains(message)) {
                    log.info("Expected Alert Message [" + message + "] not shown... Actual Message is [" + messageElement.getText() + "]");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    /***
     * This message closes Message box
     *
     * @param closeElement : Web element for the Close button.
     * @return : True or False depending on the results
     */
    public boolean closeMessage(WebElement closeElement) {
        try {
            this.eXISTs(driver, closeElement, 10);
            closeElement.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

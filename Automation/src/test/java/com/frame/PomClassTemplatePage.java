package com.frame;
import com.lib.UtilityFun;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.Hashtable;
/**
 * This is Template to create Page object model class
 *
 * @version 0.1
 * Date :
 * @author :
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */
public class PomClassTemplatePage extends UtilityFun {

    protected WebDriver driver;

    public static final String CONSTANT_NAME_1="Column Name 1";
    public static final String CONSTANT_NAME_2="Column Name 2";
    @FindBy(id = "REPLACEWITHGRIDID")
    public WebElement GRIDWEBELEMENT;
    @FindBy(id = "REPLACEID")
    public WebElement TEXTELEMENT;
    // Constructor
    public PomClassTemplatePage(WebDriver dr) {
        driver = dr ;
    }


}


package com.scripts;
import com.lib.SuiteListener;
import org.testng.annotations.*;
/**
 * This is a template for creation of new script.
 *
 * @version 0.1
 * Date:
 * @author :
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */
@Listeners({ SuiteListener.class})
public class TestClassTemplate extends BaseTestClass {
    @BeforeSuite (alwaysRun = true)
    public void customBeforeSuiteOperation(){
        log.info("This is custom BeforeSuite Method");
    }
    @BeforeMethod (alwaysRun = true)
    public  void customBeforeMethodOperation(){
        log.info("This is custom BeforeMethod Method");
    }
    @AfterMethod (alwaysRun = true)
    public void customAfterMethodOperation(){
        log.info("This is custom AfterMethod Method");
    }
    @AfterSuite (alwaysRun = true)
    public void customAfterSuiteOperation(){
        log.info("This is custom AfterSuite Method");
    }
    // This is a test case/script for an application. Replace Name with actual test case name
}

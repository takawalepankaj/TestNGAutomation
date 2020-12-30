package com.scripts;


import com.frame.*;

import com.lib.EnvSetup;
import com.lib.SuiteListener;
import com.lib.UtilityFun;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

/**
 * This is template script used by all test scripts to perform all prerequisite like setting up env and all.
 *
 *@Author :
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */


@Listeners({ SuiteListener.class})
public abstract class BaseTestClass
{

    // Instantiate Page class, utility and test environment objects
    public UtilityFun utilObj;
    public EnvSetup envSetup;
    public WebDriver driver;

    //POM page declaration
    public PageHelper pageHelper;

    // Define Logger
    protected Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * This method is executed before running any test case in test suite.
     */
    @BeforeSuite (alwaysRun = true)
    public void testSuiteSetup () {

        envSetup = new EnvSetup();

        // Initialise logger
        EnvSetup.initializeLogger();
        log.info("Logger initialized.");
        log.info("Start of Before Suite Method.");

        // Set up Browser
        EnvSetup.setupBrowserInstance(true);
        driver = EnvSetup.WEBDRIVER;

        // Initialize POM Class objects.
        pageHelper = new PageHelper(EnvSetup.WEBDRIVER);

        log.info("End of Before Suite Method.");
    }

    /**
     * This method will run before every test case.
     * This will take care of taking application back to homepage if user is already logged in otherwise it will login user.
     */
    @BeforeMethod (alwaysRun = true)
    public void testMethodSetup() {

        utilObj = new UtilityFun();
        log.info("Start of BeforeMethod Method.");
        EnvSetup.setupBrowserInstance(false);

        // Initialize Page Factory for POM Class objects.

        pageHelper = PageFactory.initElements(EnvSetup.WEBDRIVER, PageHelper.class);

        log.info("End of BeforeMethod Method.");
    }

    /**
     * This method will run after every test case execution.
     * This will switch WebDriver to default content. This is necessary in case page contains iframes
     */
    @AfterMethod (alwaysRun = true)
    public void testMethodCleanup() {
        try {
            log.info("Start of AfterMethod Method.");
            log.info("Switching to default content...");// Closing application as well as browser instance (Clean-UP activity).
            EnvSetup.WEBDRIVER.switchTo().defaultContent();
            log.info("End of AfterMethod Method.");
        } catch (Exception e) {
            log.info("Exception in afterTest...");
        }
    }

    /**
     * This method will run at the end of Test suite.
     * This will make sure that we are logging out of application and closing browser.
     */
    @AfterSuite (alwaysRun = true)
    public void testSuiteCleanUp () {
        try {
            log.info("Start of AfterSuite Method.");
            log.info("Closing Browser...");
            EnvSetup.closeBrowser();
            log.info("End of AfterSuite Method.");
        } catch (Exception e) {
            log.info("Exception in testCleanUp...");
        }
    }

}

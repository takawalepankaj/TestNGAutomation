package com.lib;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


/**
 * This generic class is used declare and initialize the environment variable which is used widely in the automation framework.
 */
public class EnvSetup {

    private static UtilityFun objFun;

    // Browser details
    public static String BROWSER_NAME;
    public static String DEFAULT_BROWSER_NAME;
    public static String DRIVER_PATH;

    public static WebDriver WEBDRIVER;

    // Suite path
    public static String SUITE_PATH = System.getProperty("user.dir")+"\\";

    // Test Data path
    public static String TEST_DATA_PATH = SUITE_PATH + "src\\test\\java\\com\\data\\";

    // Test Input Data path [Excel Files]
    public static String TEST_INPUT_DATA_PATH;

    // Test configuration File Path
    public static String MASTER_CONFIG_PATH = SUITE_PATH + "src\\test\\java\\com\\data\\config\\";
    public static String LOCALIZATION_CONFIG_PATH;

    //Application Language
    public static String APP_LANGUAGE;

    // Utilities Path
    public static String UTILITIES_PATH = SUITE_PATH + "utilities\\";

    // Log4j property file location
    public static String LOGGER_PROPERTY_FILE = SUITE_PATH + "src\\test\\java\\com\\data\\log4j.properties";

    // Screen Shot path details
    public static String SNAP_FOLDER_PATH = SUITE_PATH + "target\\surefire-reports\\html\\";
    public static String SNAP_IMAGE_WIDTH;
    public static String SNAP_IMAGE_HEIGHT;

    // Log Folder Path
    public static String LOG_FOLDER_PATH = SUITE_PATH + "target\\logs\\";

    //Execution Result Excel file
    public static String TEST_EXECUTION_RESULT_FILE = SNAP_FOLDER_PATH + "Test_Result_Summary.xls";
    public static String TEST_EXECUTION_CSV_RESULT_FILE = SNAP_FOLDER_PATH + "results.csv"; //SUITE_PATH + "target\\results\\results.csv";
    public static String TEST_EXECUTION_COUNT_FILE = SNAP_FOLDER_PATH + "tcnum.txt";

    public static boolean IS_ENVIRONMENT_READY;
    public static String USER_PROPERTY_FILE_PATH;



    public static String OS_TYPE;

    public static Hashtable hTestDetails;

    public static String TESTCASE_START_TIME;
    public static String TESTCASE_FEAT;


    public static String BAMBOO_URL;
    public static String TEST_JIRA_ID;
    public static String SKIP_COLUMN_VALUE;

    public static String RELEASE_NUMBER;
    public static String BUILD_NUMBER;
    public static String APP_URL;
    public static String APP_USER_NAME;
    public static String APP_PASSWORD;
    public static String PRE_NUMBER_FOR_DATA_DRIVEN;
    public static String LOG_LEVEL;
    public static Properties PROPERTIES_FILE;

    public static String TESTCASE_INPUT_FILE;
    public EnvSetup() {
        objFun = new UtilityFun();
        if (EnvSetup.IS_ENVIRONMENT_READY != true)
        {
            IS_ENVIRONMENT_READY = true;

            //Reading Master Config file

            USER_PROPERTY_FILE_PATH = getPropertyFilePath();

            // Browser details

            BROWSER_NAME = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","Browser");


            if(BROWSER_NAME.isEmpty()){
                BROWSER_NAME = "FIREFOX";
            }
            DEFAULT_BROWSER_NAME = BROWSER_NAME;


            DRIVER_PATH = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","DriverPath");

            if(DRIVER_PATH.isEmpty()){
                DRIVER_PATH = UTILITIES_PATH;
            }
            if (DRIVER_PATH.charAt(DRIVER_PATH.length()-1) != '\\'){
                DRIVER_PATH = DRIVER_PATH + "\\";
            }

            OS_TYPE = getWindowsOSType();

            //Error Snapshot Details

            SNAP_IMAGE_WIDTH = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","ImageWidth");
            SNAP_IMAGE_HEIGHT = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","ImageHeight");

            RELEASE_NUMBER = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","ReleaseNumber");
            BUILD_NUMBER = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","BuildNumber");

            APP_URL = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","AppUrl");

            APP_USER_NAME = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","Username");
            APP_PASSWORD = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","Password");

            objFun.createDIR(SUITE_PATH , "target");
            objFun.createDIR(SUITE_PATH + "target\\", "surefire-reports");
            objFun.createDIR(SUITE_PATH + "target\\", "logs");
            objFun.createDIR(SUITE_PATH+"target\\surefire-reports\\", "screenShots");
            objFun.createDIR(SUITE_PATH+"target\\surefire-reports\\", "html");
            objFun.createDIR(SUITE_PATH + "target\\surefire-reports\\", "xml");

            BAMBOO_URL=objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","BambooURL");
            APP_LANGUAGE = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","Language");

            PRE_NUMBER_FOR_DATA_DRIVEN = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","AddNumberForDataDriven");

            if(PRE_NUMBER_FOR_DATA_DRIVEN.isEmpty()){
                PRE_NUMBER_FOR_DATA_DRIVEN = "NO";
            }

            if(APP_LANGUAGE.isEmpty()){
                APP_LANGUAGE = "en_us";
            }

            LOG_LEVEL = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","loglevel");
            if(LOG_LEVEL.isEmpty()){
                LOG_LEVEL = "INFO";
            }

            LOCALIZATION_CONFIG_PATH = MASTER_CONFIG_PATH + "config_" + APP_LANGUAGE + ".xml";

            TESTCASE_INPUT_FILE = objFun.getXMLNodeValue(USER_PROPERTY_FILE_PATH,"WebApp","TestcaseXMLFile");

            System.out.println("Reading [" + APP_LANGUAGE + "] language configuration data from :" + LOCALIZATION_CONFIG_PATH);

            //Read data from Localization xml file
            //WARNING_MSG = objFun.getXMLNodeValue(LOCALIZATION_CONFIG_PATH,"WebApp","Warning");

            TEST_INPUT_DATA_PATH = TEST_DATA_PATH + "testinput\\" + APP_LANGUAGE + "\\";
            System.out.println("Reading Input data from :" + TEST_INPUT_DATA_PATH);

            try {
                FileWriter fw = new FileWriter(SUITE_PATH+"build.txt",false); //the true will append the new data
                fw.append(RELEASE_NUMBER+ " ("+ BUILD_NUMBER+")");
                fw.close();
            }catch (Exception e) {}

            System.out.println("Environment variables initialized...");
        }
        else
        {
         //TO-DO
        }
    }

    /**
     * Get Windows OS Type
     * @return string either 32 or 64 depending on the type.
     */
    public String getWindowsOSType(){
        try{
            if (System.getenv("ProgramW6432")!= null){
                return "64";
            }
            return "32";
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Get Property File Path
     * @return : String Path of the property file
     */
    private String getPropertyFilePath() {
        String sPropertyFileName = "";
        if (System.getenv("configpath") != null){
            String sTemp = System.getenv("configpath");
            System.out.println("Verifying configuration file present @location System Environment variable [" + sTemp + "]");
            if (sTemp.charAt(sTemp.length()-1) == '\\'){
                sPropertyFileName = sTemp + "config.xml";
            }
            else {
                sPropertyFileName = sTemp + "\\config.xml";
            }
            if (!objFun.isFileExists(sPropertyFileName)){
                System.out.println("Verifying configuration file present @location [" + System.getenv("userprofile") +"]");
                sPropertyFileName = System.getenv("userprofile")+"\\config.xml";
                if (!objFun.isFileExists(sPropertyFileName)){
//sPropertyFileName = System.getProperty("user.dir")+"\\src\\test\\java\\com\\data\\config.properties";
                    sPropertyFileName = MASTER_CONFIG_PATH + "config.xml";
                }
            }
        }
        else {
            System.out.println("Verifying configuration file present @location [" + System.getenv("userprofile") +"]");
            sPropertyFileName = System.getenv("userprofile")+"\\config.xml";
            if (!objFun.isFileExists(sPropertyFileName)){
//sPropertyFileName=System.getProperty("user.dir")+"\\src\\test\\java\\com\\data\\config.properties";
                sPropertyFileName = MASTER_CONFIG_PATH + "config.xml";
            }
        }

        System.out.println("Reading configuration parameters from [" + sPropertyFileName +"] location");
        return sPropertyFileName;
    }

    /**
     * Create Excel Results file.
     */
    private void createExcelResultFile() {

        try{
            Workbook workbook = null;
            Sheet sheet =null;
            Row row =null;

//Check if Test Execution result excel file exists
            if (objFun.isFileExists(EnvSetup.TEST_EXECUTION_RESULT_FILE)) {
                return;
            }

            OutputStream inp = new FileOutputStream(EnvSetup.TEST_EXECUTION_RESULT_FILE);

            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet("Sheet1");

            row = sheet.createRow(0);

            row.createCell(0).setCellValue("TestCaseName");
            row.createCell(1).setCellValue("ExecutionStartDate");
            row.createCell(2).setCellValue("ExecutionEndDate");
            row.createCell(3).setCellValue("ExecutionTime");
            row.createCell(4).setCellValue("Status");
            row.createCell(5).setCellValue("ErrorMessage");
            row.createCell(6).setCellValue("FEAT");

            workbook.write(inp);
            inp.close();

        }catch(Exception e) {
        }
    }

    /**
     * This method will start Specific browser driver and open up browser.
     * @param driver of WebDriver Type
     * @return WebDriver instance of
     */
    public static WebDriver setupDriverInstance(WebDriver driver) {
// Determine the browser type and Define browser type
        Browsers browser;
//WebDriver driver;

        if (driver == null ) {
            browser = Browsers.browserForName(BROWSER_NAME);

            switch (browser) {
                case FIREFOX :
//log.info("Instantiate FF browser");
                    driver = createFirefoxDriver ();
                    break;
                case IE :
//log.info("Instantiate IE browser");
                    driver = createIEDriver ();
                    break;
                case CHROME:
//log.info("Instantiate IE browser");
                    driver = createChromeDriver ();
                    break;
                case SAFARI:
//log.info("Instantiate IE browser");
                    driver = createSafariDriver();
                    break;
                default:
//log.info("Instantiate Default FireFox browser");
                    BROWSER_NAME = "FIREFOX";
                    driver = createFirefoxDriver ();
                    break;
            }
            driver=updateBrowserSettings(driver);
        }
        try {
            //sleep(5000);
        }catch(Exception e) {}
        return driver;
    }

    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }

    /**
     * Create Chrome Driver instance and start Chrome browser
     * @return : Web Driver instance
     */
    private static WebDriver createChromeDriver() {
        File file = new File(DRIVER_PATH +"chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
//System.setProperty("Always.ask.where.to.save.files", true);
        Map<String, String> prefs = new Hashtable<String, String>();
        prefs.put("download.prompt_for_download", "true");
        prefs.put("download.default_directory", "C:\\");
        prefs.put("download.extensions_to_open", "pdf");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.prefs", prefs);
        capabilities.setCapability("chrome.switches", Arrays.asList("--user-data-dir="+System.getProperty("user.dir")+"\\AppData\\Local\\Google\\Chrome\\User Data\\Default"));
        return new ChromeDriver(capabilities);
    }

    /**
     * Create Firefox Driver instance and start Firefox browser.
     * @return : Web Driver Instance.
     */
    private static WebDriver createFirefoxDriver() {
        FirefoxProfile prof = new FirefoxProfile();
//prof.setEnableNativeEvents(true);
        prof.setPreference("browser.download.folderList", 2);
//prof.setPreference("browser.download.dir", System.getProperty("user.dir"));
//prof.setPreference("browser.download.dir", System.getProperty("user.dir") + "\\target");
        if (System.getenv("ProgramW6432")!= null)
            prof.setPreference("browser.download.dir", System.getProperty("user.home") + "\\Downloads");
        else
            prof.setPreference("browser.download.dir", System.getProperty("user.home") + "\\My Documents\\Downloads");
//prof.setPreference("plugin.disable_full_page_plugin_for_types", "application/zip,application/msword,application/pdf,text/csv,aplication/csv,application/octet-stream");
//prof.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf, text/x-patch,application/x-jar,application/vnd.android.package-archive,application/zip,application/msword, text/comma-separated-values, text/csv, application/csv, application/excel, application/vnd.ms-excel, application/vnd.msexcel, text/anytext,application/octet-stream");
        return new FirefoxDriver(prof);
    }

    /**
     * This will start IE WebDriver and starts IE Browser
     * @return WebDriver for IE
     */
    private static WebDriver createIEDriver() {

//File file = new File(DRIVER_PATH +"IEDriverServer_"+ OS_TYPE +".exe");
        File file = new File(DRIVER_PATH +"IEDriverServer.exe");
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
//WebDriver driver = new InternetExplorerDriver();
        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
//caps.setCapability("ignoreZoomSetting", true);
        caps.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
        caps.setCapability("requireWindowFocus",true);

        return new InternetExplorerDriver(caps);
    }

    /**
     * This will take care of doing specific setting related to browsers
     * @param driver : Instance of WebDriver
     * @return : Web Driver instance with updated settings.
     */
    private static WebDriver updateBrowserSettings(WebDriver driver) {

//Set implicit time for Web Elements to be available
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

// Set script execution timeout
        driver.manage().timeouts().setScriptTimeout(60,TimeUnit.SECONDS);

        if (!BROWSER_NAME.equalsIgnoreCase("safari")) {
// Set page load timeout
            driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
        }
//driver.manage().deleteAllCookies();

//driver.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);

// Maximize the browser window
        driver.manage().window().maximize();
        return driver;
    }

    public enum Browsers {
        SAFARI,
        FIREFOX,
        CHROME,
        IE,
        NOBROWSER;

        public static Browsers browserForName(String browser) throws IllegalArgumentException{
            for(Browsers b: values()){
                if(b.toString().equalsIgnoreCase(browser)){
                    return b;
                }
            }
            return NOBROWSER;
//throw browserNotFound(browser);
        }

        private static IllegalArgumentException browserNotFound(String outcome) {
            return new IllegalArgumentException(("Invalid browser [" + outcome + "]"));
        }
    }

    /**
     * This Method will setup driver instance.
     * @return : It returns the WEBDRIVER depending on the browser type set in config/Test/Suite Level.
     */
    public synchronized static WebDriver getCurrentDriver() {
//Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("dataFile1");
        if (WEBDRIVER==null) {
            try {
                WEBDRIVER = setupDriverInstance(WEBDRIVER) ;
//WEBDRIVER1 = setupDriverInstance(WEBDRIVER1) ;
            }
            catch (Exception e) {
            }
        }
        return WEBDRIVER;
    }

    /**
     * This method will close respective browser and quites web driver for it.
     */
    public static void closeBrowser() {
        try {
//Verify web-driver is empty or not
            if (WEBDRIVER != null) {
                WEBDRIVER.quit();
                WEBDRIVER = null;

            }
            sleep(5 * 1000);
        }
//LOGGER.info("closing the browser");
        catch (Exception e) {
//LOGGER.info("cannot close browser: unreachable browser");
        }

    }

    private static class BrowserCleanup implements Runnable {
        public void run() {
            closeBrowser();
        }
    }

    public static void loadPage(String url) {
        getCurrentDriver();
//LOGGER.info("Directing browser to:" + url);
//LOGGER.info("try to loadPage [" + url + "]");
        getCurrentDriver().get(url);
    }

    private String getConfigFilePath() {

        String sCPath = null;
        String sConfigFileName = "";
        try {
            sCPath = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "Environment", "configpath");

            if (sCPath == null)
            {
                sCPath= WinRegistry.readString( WinRegistry.HKEY_LOCAL_MACHINE,"SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment","configpath");//System.getenv("configpath");
            }
        } catch (Exception e) {

        }

        if (sCPath != null){
            String sTemp = sCPath;
            if (sTemp.charAt(sTemp.length()-1) == '\\'){
                sConfigFileName = sTemp + "config.xml";
            }
            else {
                sConfigFileName = sTemp + "\\config.xml";
            }
            if (!objFun.isFileExists(sConfigFileName)){
                sConfigFileName = System.getenv("userprofile")+"\\config.xml";
                if (!objFun.isFileExists(sConfigFileName)){
                    sConfigFileName=System.getProperty("user.dir")+"\\src\\test\\java\\com\\data\\config.xml";
                }
            }
        }
        else {
            sConfigFileName = System.getenv("userprofile")+"\\config.xml";
            if (!objFun.isFileExists(sConfigFileName)){
                sConfigFileName=System.getProperty("user.dir")+"\\src\\test\\java\\com\\data\\config.xml";
            }
        }

        System.out.println("Reading configuration parameters from [" + sConfigFileName +"] location");
        return sConfigFileName;


    }

    /**
     This method is used to launch the test specific/Sutie Specific browser for execution.
     *
     * @param bSuiteLevel true if @BeforeSuite level otherwise false (if @test notation)
     *
     */
    public static void setupBrowserInstance(boolean bSuiteLevel){
//Get Suite Browser Name
        String sSuiteBrowser; // = EnvSetup.DEFAULT_BROWSER_NAME;
/* if (!bSuiteLevel) {
sSuiteBrowser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("browser"); //SuiteListener.getAccess().getParameter("browser");
if (sSuiteBrowser == null ) {
sSuiteBrowser = EnvSetup.DEFAULT_BROWSER_NAME;
}
} */
        try {
//sSuiteBrowser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("browser"); //SuiteListener.getAccess().getParameter("browser");

            if (bSuiteLevel) {
                sSuiteBrowser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
            } else {
                sSuiteBrowser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("browser");
            }

            if (sSuiteBrowser == null ) {
                sSuiteBrowser = EnvSetup.DEFAULT_BROWSER_NAME;
            }
        }catch (Exception e) {
            sSuiteBrowser = EnvSetup.DEFAULT_BROWSER_NAME;
        }

        if (!EnvSetup.BROWSER_NAME.equalsIgnoreCase(sSuiteBrowser)) {
            if (EnvSetup.WEBDRIVER !=null){
                EnvSetup.closeBrowser();
            }
            EnvSetup.BROWSER_NAME = sSuiteBrowser.toUpperCase();
        }

        EnvSetup.getCurrentDriver();
    }

    public enum dateOption {
        ALLDates,
        NoOfDaysPriorToNow,
        BetweenDates
    }

    /**
     * This method will initialize logger.
     */
    public static void initializeLogger()
    {
        new InitLog4j(objFun.TestSuiteName());
    }




} 
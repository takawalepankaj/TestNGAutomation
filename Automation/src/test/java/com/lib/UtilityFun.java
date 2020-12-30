package com.lib;



import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


import java.lang.reflect.TypeVariable;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A generic utility function class used across automation framework.
 *
 * @version 0.1 -
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */

@Listeners({ SuiteListener.class })
public class UtilityFun implements IRetryAnalyzer,IAnnotationTransformer {

    // Define logger
    protected Logger log = Logger.getLogger(this.getClass().getName());
    private int count = 0;
    // this number is actually twice the number of retry attempts will allow due to the retry method being called twice for each retry
    private int maxCount = 0;

    /**
     * This method is used to verify specified object exists or not.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     * @param secs Sepcify time in seconds to waitfor
     *
     * @return WebElement object
     *
     */
    public WebElement eXISTs(WebDriver driver, By by, int secs) {
// Declare a dynamic element
        WebElement dynaElement = null;
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Declare an explicit wait
            WebDriverWait wait1 = new WebDriverWait(driver, secs);
            dynaElement = wait1.until(ExpectedConditions.presenceOfElementLocated(by));
            return (dynaElement);
        }
        catch (Exception e){
//log.debug ("Exception occurred in eXISTs method...");
            return dynaElement;
        }
    }

    /**
     * This method is used to verify specified WebElement object exists or not.
     *
     * @param driver Selenium WebDriver object
     * @param element Selenium WebElement object
     * @param secs Sepcify time in seconds to waitfor
     *
     * @return WebElement object
     *
     */
    public WebElement eXISTs(WebDriver driver, WebElement element, int secs) {
// Declare a dynamic element
        WebElement dynaElement = null;
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Declare an explicit wait
            WebDriverWait wait = new WebDriverWait(driver, secs);
            dynaElement = wait.until(ExpectedConditions.visibilityOf(element));

            return (dynaElement);
        }
        catch (Exception e){
//log.error("Exception occurred in eXISTs method...");
            return dynaElement;
        }
    }

    /**
     *
     * @param driver : Selenium WebDriver Object
     * @param title : Title of the Web Page
     * @param secs : Specify time in seconds to Wait
     */
    public void waitForPage(WebDriver driver, String title, int secs) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Declare an explicit wait
            WebDriverWait wait = new WebDriverWait(driver, secs);
            wait.until(ExpectedConditions.titleContains(title));
//wait.until(ExpectedConditions.presenceOfElementLocated(by));

// Wait for page to load completely
            JavascriptExecutor js = (JavascriptExecutor) driver;
// Check the state
            for (int i = 5; i <= secs; ) {
                this.wait(5);
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
                i += 5;
            }
        }
        catch (Exception e){
            log.error("Exception occurred in waitForPage method...");
        }
    }

    /**
     * This method is used to verify specified Web Page exists or not.
     *
     * @param driver Selenium WebDriver object
     * @param by of the xpath of webelement
     * @param secs Specify time in seconds to waitfor
     *
     * @return boolean return true if specified WebElement found otherwise false.
     *
     */
    public boolean waitForPage(WebDriver driver, By by, int secs) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Declare an explicit wait
            WebDriverWait wait = new WebDriverWait(driver, secs);
//wait.until(ExpectedConditions.titleContains(title));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));

// Wait for page to load completely
            JavascriptExecutor js = (JavascriptExecutor) driver;
// Check the state
            for (int i = 5; i <= secs; ) {
                this.wait(5);
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
                i += 5;
            }
            return true;
        }
        catch (Exception e){
            log.error("Exception occurred in waitForPage method...");
            return false;
        }
    }

    /**
     * This method is used to verify specified text exists or not.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     *
     * @return boolean true/false value
     *
     */
    public boolean isTextPresent(WebDriver driver, By by) {
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            driver.findElement(by);
            return (true);
        } catch (Exception e) {
            log.error("Exception occurred in isTextPresent method...");
            return (false);
        }

    }

    /**
     * This method is used to wait for the webDriver till specified time..
     *
     * @param driver Selenium WebDriver object
     */
    public void nullifyImplicitWait(WebDriver driver) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Nullify implicit wait
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(0,TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(0,TimeUnit.SECONDS);
        }
        catch (Exception e){
            log.error("Exception occurred in {nullifyImplicitWait} method...");
        }
    }

    /**
     * This method is used to user defined time for webDriver.
     *
     * @param driver Selenium WebDriver object
     * @param secs Specify time in seconds to waitfor
     *
     */
    public void setImplicitWait(WebDriver driver, int secs) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Set implicit wait
            driver.manage().timeouts().implicitlyWait(secs, TimeUnit.SECONDS);
        }
        catch (Exception e){
            log.error("Exception occurred in {setImplicitWait} method...");
        }
    }

    /**
     * This method is used to switch from one window to another.
     *
     * @param driver Selenium WebDriver object
     * @param title Title of the webpage
     *
     */
    public void switchWindow(WebDriver driver, String title) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get all open window handles
            Set<String> windowHandles = driver.getWindowHandles();
// Switch to respective window
            if (!windowHandles.isEmpty()) {
                for (String windowId : windowHandles) {
                    if (driver.switchTo().window(windowId).getTitle().contains(title)) {
                        break;
                    } else {
// Switch to main window (first one)
                        driver.switchTo().defaultContent();
                    }
                }
            }
        }
        catch (Exception e){
            log.error("Exception occurred in {switchWindow} method...");
        }
    }

    /**
     * This method is used to switch from window to Frame Object.
     *
     * @param driver Selenium WebDriver object
     * @param id Name of the frame
     *
     */
    public void switchFrame(WebDriver driver, String id) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Define a dynamic element
            WebElement frameElement = driver.findElement(By.xpath("//iframe[contains (@id," + "'" + id + "'" + ")]"));
// Switch to the frame by an element
            driver.switchTo().frame(frameElement);
        }
        catch (Exception e){
            log.error("Exception occurred in {switchFrame} method...");
        }
    }

    /**
     * This method is used to set focus on specified object.
     *
     * @param driver Selenium WebDriver object
     * @param elementID Name of the element
     *
     */
    public void setFocus(WebDriver driver, String elementID) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Set Focus on an element using JS method
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("document.getElementById('" + elementID + "').focus()");
        }
        catch (Exception e){
            log.error("Exception occurred in {setFocus} method...");
        }
    }


    /**
     * This method is used to check whether any java script error dialog popup or not.
     *
     * @param driver Selenium WebDriver object
     * @return boolean true/false value
     *
     */
    public boolean alertExists(WebDriver driver) {

// check if alert does exist
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            driver.switchTo().alert();
            System.out.println("An Alert message appears");
            return true;
        } catch (Exception e) {
            log.error("Exception occurred in {alertExists} method...");
            return false;
        }
    }

    /**
     * This method is used to Close the existing Alert message.
     *
     * @param driver Selenium WebDriver object
     *
     * @return String shows Alert message
     *
     */
    public String closeAlert(WebDriver driver, String closureOption) {

// Determine closure option based on option
        boolean bFlag = (closureOption.equalsIgnoreCase("accept")) ? true : false;
        String alertText = "";
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            if (alertExists(driver)) {
                Alert alert = driver.switchTo().alert();
                alertText = alert.getText();
// Clear the alert message
                if (bFlag) {
                    alert.accept();
                } else {
                    alert.dismiss();
                }
            }
        }
        catch (Exception e){
            log.error("Exception occurred in {closeAlert} method...");

        }
// Return captured Alert Message
        return (alertText);
    }

    /**
     * This method is used to get specified WebElement which is visible to the page.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     * @param textORattribute Attribute Name
     * @param txt
     *
     * @return WebElement Get visible element from webpage
     *
     */
    public WebElement getVisibleElement(WebDriver driver, By by, char textORattribute, String txt) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get all Tree view items
            java.util.List<WebElement> lsItems = driver.findElements(by);
// Find whether the tree view item exist or not
            for (WebElement itm : lsItems) {
                switch (textORattribute) {
                    case 't':
                        if (itm.getText().equals(txt)) {
                            return (itm);
                        }
                    case 'a':
                        if (itm.getAttribute("value").equals(txt)) {
                            return (itm);
                        }
                    default:
// YTD
                        break;
                }
            }
        }
        catch (Exception e){
            log.error("Exception occurred in {getVisibleElement} method...");
        }
        return (null);
    }

    /**
     * This method is used to Click on OK button.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     *
     */
    public void clickOK(WebDriver driver, By by) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get all handles of OK button
            java.util.List<WebElement> lsHandles = driver.findElements(by);

// Find whether the tree view item exist or not
            for (WebElement itm : lsHandles) {
                if (itm.isDisplayed()) {
                    itm.click();
                    break;
                }
            }

            this.wait(2);
        }
        catch (Exception e){
            log.error("Exception occurred in {clickOK} method...");

        }
    }

    /**
     * This method is used to to Click on Apply button.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     *
     */
    public void clickApply(WebDriver driver, By by) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get all handles of Apply button
            java.util.List<WebElement> lsHandles = driver.findElements(by);
// Find whether the tree view item exist or not
            for (WebElement itm : lsHandles) {
                if (itm.isDisplayed()) {
                    itm.click();
                    break;
                }
            }
        }
        catch (Exception e){
            log.error("Exception occurred in {clickApply} method...");
        }
    }

    /**
     * This method is used to to Click on Cancel button.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     *
     */
    public void clickCancel(WebDriver driver, By by) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get all handles of Cancel button
            java.util.List<WebElement> lsHandles = driver.findElements(by);
// Find whether the tree view item exist or not
            for (WebElement itm : lsHandles) {
                if (itm.isDisplayed()) {
                    itm.click();
                    break;
                }
            }
        }
        catch (Exception e){
            log.error("Exception occurred in {clickCancel} method...");
        }
    }

    /**
     * This method is used to get specified size of the object in Rectangle format.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     *
     * @return Rectangle object
     *
     */
    public Rectangle getRelativeSize(WebDriver driver, By by) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get starting point of an object first
            Point p = driver.findElement(by).getLocation();
// Get size of an object
            Dimension dim = driver.findElement(by).getSize();
// Generate a size of an object
            Rectangle rect = new Rectangle(p.x, p.y + 100, dim.width, dim.height);
// System.out.println(rect);
            return (rect);
        }
        catch (Exception e){
            log.error("Exception occurred in {getRelativeSize} method...");
            return null;
        }
    }

    /**
     * This method is used to get specified size of the object in Rectangle format.
     *
     * @param element WebElement Object from the web page.
     *
     * @return Rectangle object
     *
     */
    public Rectangle getRelativeSize(WebElement element) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Get starting point of an object first
            Point p = element.getLocation();
// Get size of an object
            Dimension dim = element.getSize();
// Generate a size of an object
            Rectangle rect = new Rectangle(p.x, p.y + 100, dim.width, dim.height);
// System.out.println(rect);
            return (rect);
        }
        catch (Exception e){
            log.error("Exception occurred in {getRelativeSize} method...");
            return null;
        }
    }

    /**
     * This method is used to double click on specified object on web page.
     *
     * @param driver Selenium WebDriver object
     * @param by Selenium By object which contains xpath/ID/Name details
     *
     */
    public void doubleClick(WebDriver driver, By by) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Identify an element
            WebElement wElm = driver.findElement(by);

// Create a new action for doubleClick
            Actions action = new Actions(driver);
            action.doubleClick(wElm);
            action.perform();
        }
        catch (Exception e){
            log.error("Exception occurred in {doubleClick} method...");
        }
    }

    /**
     * This method is used to double click on specified object on web page.
     *
     * @param driver Selenium WebDriver object
     * @param wElm WebElement Object from the web page.
     *
     */
    public void doubleClick(WebDriver driver, WebElement wElm) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Create a new action for doubleClick
            Actions action = new Actions(driver);
            action.doubleClick(wElm);
            action.perform();
        }
        catch (Exception e){
            log.error("Exception occurred in {doubleClick} method...");
        }
    }

    /**
     * This method is used to drag the specified object.
     *
     * @param driver Selenium WebDriver object
     * @param dBy Selenium By object which contains xpath/ID/Name details
     *
     */
    public void dragANDdrop(WebDriver driver, By sBy, By dBy) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Identify Source and Destination elements
            WebElement source = driver.findElement(sBy);
            WebElement destin = driver.findElement(dBy);

// Create an action for drag and drop
            Actions actions = new Actions(driver);
            actions.dragAndDrop(source, destin);
            actions.perform();
        }
        catch (Exception e){
            log.error("Exception occurred in {dragANDdrop} method...");
        }
    }

    /**
     * This method is used to drag and drop the specified object.
     *
     * @param driver Selenium WebDriver object
     * @param source WebElement Object from the web pages
     * @param destin WebElement Object from the web pages
     *
     */
    public void dragANDdrop(WebDriver driver, WebElement source, WebElement destin) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Create an action for select and drag
            Actions drag = new Actions(driver);
            drag.clickAndHold(source).perform();
// Create an action for drop
            Actions drop = new Actions(driver);
            drop.moveToElement(destin).release().perform();

// action.dragAndDrop(source, destin);
        }
        catch (Exception e){
            log.error("Exception occurred in {dragANDdrop} method...");
        }
    }

    /**
     * This method is used to check if file exists or not.
     *
     * @param sFileName Name of file.
     * @return boolean Return true if file exists otherwise false.
     *
     */
    public boolean isFileExists(String sFileName){
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            File file = new File(sFileName);
            boolean exists = file.exists();

            if (file.exists()) {
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     * This method is used to read value from AAPC property file located in data folder.
     *
     * @param sKey Property key name
     *
     * @return String Value of the key specified
     *
     */
    public String readPropertyValue(String sKey) {

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sPropertyFileName=EnvSetup.USER_PROPERTY_FILE_PATH;
        String sValue;
        FileInputStream fs = null;
        try
        {
            fs = new FileInputStream(sPropertyFileName);;
            Properties objConfigOther = new Properties();

            objConfigOther.load(fs);
            sValue=objConfigOther.getProperty(sKey);
        }
        catch (Exception e) {
            log.error("Exception occurred in {ReadPropertyValue} method...");
            sValue = null;
        }
        finally {
            try {
                if (fs!= null) {
                    fs.close();
                }
            }
            catch (Exception e) {

            }
        }
        return sValue;
    }

    /**
     * This method is used to read value from property file.
     *
     * @param sKey Property key name
     * @param sPropertyFileName Name of the Property file.
     *
     * @return String Value of the key specified
     *
     */
    public String readPropertyValue(String sKey,String sPropertyFileName) {

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sValue;
        FileInputStream fs = null;
        try
        {
            fs = new FileInputStream(sPropertyFileName);;
            Properties objConfigOther = new Properties();

            objConfigOther.load(fs);
            sValue=objConfigOther.getProperty(sKey);
        }
        catch (Exception e) {
            log.error("Exception occurred in {ReadPropertyValue} method...");
            sValue = null;
        }
        finally {
            try {
                if (fs!= null) {
                    fs.close();
                }
            }
            catch (Exception e) {

            }
        }
        return sValue;
    }

    /**
     * This method is used to get date value in user defined format.
     *
     *
     * @return String Date value in user defined format.
     *
     */
    public String getDate() throws ParseException {

        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strDate = sdf.format(cal.getTime());

            SimpleDateFormat sdf1 = new SimpleDateFormat();
            sdf1.applyPattern("dd/MM/yyyy");
            Date date = sdf1.parse(strDate);
            String string=sdf1.format(date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            return (string.replaceAll("/","_"));
        }
        catch (Exception e) {
            log.error("Exception occurred in {getDate} method...");
            return null;

        }
    }



    /**
     * This method is used to Create user defined directory if does not exists.
     *
     * @param path Source file Name
     * @param folder Destimation file name.
     *
     */
    public void createDIR(String path, String folder) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Construct a path with new folder name
            File dir = new File(path + folder);
// Check if it does exist already
            if (dir.exists()) {
                log.debug("Directory "+path+folder+" already does exist");
            } else {
                dir.mkdir();
            }
        }
        catch (Exception e) {
            log.error("Exception occurred in {createDIR} method...");

        }
    }

    /**
     * This method is used to wait execution or process till user defined time.
     *
     * @param secs time specified in seconds
     *
     */
    public void wait(int secs) {

// call a native sleep
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            Thread.sleep(secs * 1000);
        } catch (Exception ex) {
            log.error("Exception occurred in {createDIR} method...");

        }
    }

    /**
     * This method is used to retrieve the excel row details containing test case name.
     *
     * @param xlsFile Excel file name
     * @param sheetName Excel worksheet name.
     * @param tcID TestCase Name.
     * @return List Excel Row data in list of rows..
     *
     */
    public java.util.List readXLSrow(String xlsFile, String sheetName, String tcID) {

        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        java.util.List<String> line = null;

        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Open the file for read
            InputStream inp = new FileInputStream(xlsFile);
            line = new ArrayList();

            workbook = WorkbookFactory.create(inp);
            sheet = workbook.getSheet(sheetName);

// Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = sheet.iterator();
// Iterate through each rows
            while (rowIterator.hasNext()) {
// Get each row
                row = rowIterator.next();
// Get iterator for all columns to current row
                Iterator<Cell> cellIterator = row.cellIterator();
// Find the row with required tcID
                if (cellIterator.next().getStringCellValue().equals(tcID)) {
// Iterate through each columns on filtered row
                    while (cellIterator.hasNext()) {
                        cell = cellIterator.next();
                        line.add(cell.getStringCellValue());
                    }
                    break;
                } else {
                    continue;
                }
            }
// Close the file handle
            inp.close();
        } catch (Exception ex) {
            log.error("Exception occurred in {readXLSrow} method...");

        }
        return (line);
    }

    /**
     * This method is used to retrieve complete excel worksheet row details in array.
     *
     * @param xlsFile Excel file name
     * @param sheetName Excel worksheet name.
     *
     * @return List Excel Row data in list of rows.
     *
     */
    public java.util.List readXLS(String xlsFile, String sheetName) {

        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        java.util.List<String> line = null;

        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Open the file for read
            InputStream inp = new FileInputStream(xlsFile);
            line = new ArrayList();

            workbook = WorkbookFactory.create(inp);
            sheet = workbook.getSheet(sheetName);

// Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = sheet.iterator();
// Iterate through each rows
            while (rowIterator.hasNext()) {
// Get each row
                row = rowIterator.next();
// Get iterator for all columns to current row
                Iterator<Cell> cellIterator = row.cellIterator();
// Iterate through each columns on current row
                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    line.add(cell.getStringCellValue());
                }
            }
// Close the file handle
            inp.close();
        } catch (Exception ex) {
            log.error("Exception occurred in {readXLS} method...");

        }
        return (line);
    }

    /**
     * This method is used to Archive the user defined folder.
     *
     * @param sSourceFolderName Source file Name
     * @param sZipFileName Name of the zip file.
     *
     */
    public void ZipFolder(String sSourceFolderName, String sZipFileName) throws IOException {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            File directoryToZip = new File(sSourceFolderName);
            if (!directoryToZip.exists()){return;}
            List<File> fileList = new ArrayList<File>();
            System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
            getAllFiles(directoryToZip, fileList);
            System.out.println("---Creating zip file");
            writeZipFile(directoryToZip, fileList,sZipFileName);
            System.out.println("---Done");
        }
        catch (Exception e) {
            log.error("Exception occurred in {ZipFolder} method...");

        }
    }

    /**
     * This method will get all files list for a given directory.
     * @param dir
     * @param fileList
     */
    public void getAllFiles(File dir, List<File> fileList) {
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            File[] files = dir.listFiles();
            for (File file : files) {
                fileList.add(file);
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    getAllFiles(file, fileList);
                } else {
                    System.out.println(" file:" + file.getCanonicalPath());
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * This method will create ZIP file for given list of files in directory.
     * @param directoryToZip
     * @param fileList
     * @param sZipFileName
     */
    public void writeZipFile(File directoryToZip, List<File> fileList,String sZipFileName) {

        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            FileOutputStream fos = new FileOutputStream(sZipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : fileList) {
                if (!file.isDirectory()) { // we only zip files, not directories
                    addToZip(directoryToZip, file, zos);
                }
            }

            zos.close();
            fos.close();
        } catch (Exception e) {

        }
    }

    /**
     * This will add files to ZIP
     * @param directoryToZip
     * @param file
     * @param zos
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            FileInputStream fis = new FileInputStream(file);

// we want the zipEntry's path to be a relative path that is relative
// to the directory being zipped, so chop off the rest of the path
            String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                    file.getCanonicalPath().length());
            System.out.println("Writing '" + zipFilePath + "' to zip file");
            ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            zos.closeEntry();
            fis.close();
        } catch (Exception e) {

        }
    }

    /**
     * This method is used to create screen shot for the running webpage and stored to user specified location in .png format.
     *
     * @param dr Selenium WebDriver object
     * @param sSnapFolderName Name of the folder where error snap get stored.
     * @param sSnapName Name of the snapshot file name.
     *
     * @return String Screen shot File Name.
     *
     */
    public String takeWebPageScreenShot(WebDriver dr, String sSnapFolderName, String sSnapName) {
        String sScrnFileName = "";
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            File fScreenshot = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
            Date date = new Date();
            sScrnFileName = sSnapName + "_" + dateFormat.format(date) + ".png";

            String sPath = sSnapFolderName + sScrnFileName;
            FileUtils.copyFile(fScreenshot, new File(sPath));
            System.setProperty("org.uncommons.reportng.escape-output", "false");

            String sHref = "<a href=" + "\"./" + sScrnFileName + "\"" + " target='_blank'>" + sScrnFileName + "</a></br>";
            Reporter.log(sHref);

            String sImg = "<img src=" + "\"" + sPath +"\"" + " height=" + "\""+ EnvSetup.SNAP_IMAGE_WIDTH + "\" width="+ "\""+ EnvSetup.SNAP_IMAGE_HEIGHT +"\"></br>";
            String sOnMOver="onmouseover=\"this.width="+ Integer.parseInt(EnvSetup.SNAP_IMAGE_WIDTH) * 2 + ";this.height="+Integer.parseInt(EnvSetup.SNAP_IMAGE_HEIGHT) * 2+ ";\"";
            String sOnMOut=" onmouseout=\"this.width="+ EnvSetup.SNAP_IMAGE_WIDTH + ";this.height="+ EnvSetup.SNAP_IMAGE_HEIGHT + ";\"";

            sImg ="<img src=" + "\"./" + sScrnFileName +"\"" + " height=" + "\""+ EnvSetup.SNAP_IMAGE_WIDTH + "\" width="+ "\""+ EnvSetup.SNAP_IMAGE_HEIGHT +"\" " + sOnMOver + sOnMOut +"></br>";

            Reporter.log(sImg);
        }
        catch (Exception e) {
            log.error("Exception occurred in {takeScreenShot} method...");
            log.info(e.getStackTrace());
            e.printStackTrace();
        }
        return sScrnFileName;
    }

    public String takeDesktopScreenShot(String sSnapFolderName, String sSnapName) {
        String sScrnFileName = "";
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
            Date date = new Date();
            sScrnFileName = sSnapName + "_" + dateFormat.format(date) + ".png";

            String sPath = sSnapFolderName + sScrnFileName;

            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage = robot.createScreenCapture(captureSize);

            File fScreenshot= new File(sPath);
            wait(3);

            boolean isSuccess = ImageIO.write(bufferedImage, "png", fScreenshot);

            System.setProperty("org.uncommons.reportng.escape-output", "false");

            String sHref = "<a href=" + "\"./" + sScrnFileName + "\"" + " target='_blank'>" + sScrnFileName + "</a></br>";
            Reporter.log(sHref);

            String sImg = "<img src=" + "\"" + sPath +"\"" + " height=" + "\""+ EnvSetup.SNAP_IMAGE_WIDTH + "\" width="+ "\""+ EnvSetup.SNAP_IMAGE_HEIGHT +"\"></br>";
            String sOnMOver="onmouseover=\"this.width="+ Integer.parseInt(EnvSetup.SNAP_IMAGE_WIDTH) * 2 + ";this.height="+Integer.parseInt(EnvSetup.SNAP_IMAGE_HEIGHT) * 2+ ";\"";
            String sOnMOut=" onmouseout=\"this.width="+ EnvSetup.SNAP_IMAGE_WIDTH + ";this.height="+ EnvSetup.SNAP_IMAGE_HEIGHT + ";\"";

            sImg ="<img src=" + "\"./" + sScrnFileName +"\"" + " height=" + "\""+ EnvSetup.SNAP_IMAGE_WIDTH + "\" width="+ "\""+ EnvSetup.SNAP_IMAGE_HEIGHT +"\" " + sOnMOver + sOnMOut +"></br>";

            Reporter.log(sImg);
        }
        catch (Exception e) {
            log.error("Exception occurred in {takeScreenShot} method...");
//log.info(e.getStackTrace().);
            e.printStackTrace();
        }
        return sScrnFileName;
    }

    private String getFileName(String nameTest) throws IOException {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
            Date date = new Date();
            return dateFormat.format(date) + "_" + nameTest + ".png";
        }
        catch (Exception e) {
            log.error("Exception occurred in {getFileName} method...");
            return "";
        }
    }

    private String getScrPath(String nameTest) throws IOException {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            File directory = new File(".");
            String newFileNamePath = directory.getCanonicalPath() + "\\target\\surefire-reports\\screenShots\\" + getFileName(nameTest);
            return newFileNamePath;
        }
        catch (Exception e) {
            log.error("Exception occurred in {getPath} method...");
            return "";
        }
    }

    @Override
    public boolean retry(ITestResult result) {

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (!result.isSuccess()){
            if(count < maxCount) {
                count++;

                log.debug("running retry logic for '"
                        + result.getName()
                        + "' on class " + this.getClass().getName());

                log.debug(Thread.currentThread().getName() + "Error in "
                        + result.getName() + " with status "
                        + result.getStatus() + " Retrying " + count + " times");

                return true;
            }
        }
        return false;
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod){

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();

        if (retry == null) {

            annotation.setRetryAnalyzer(UtilityFun.class);

        }

    }

    /**
     * This method is used to rename existing file.
     *
     * @param sSource Source file Name
     * @param sDestination Name of the file to rename.
     *
     */
    public void RenameFile(String sSource, String sDestination) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            if(new File(sSource).renameTo(new File(sDestination))) {
                System.out.println("renamed");
            } else {
                System.out.println("Error");
            }
        }
        catch (Exception e) {
            log.error("Exception occurred in {RenameFile} method...");

        }
    }

    /**
     * This method is used to rename existing file.
     *
     *
     * @return String Name of the Suite.
     *
     */
    public String TestSuiteName(){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            return (SuiteListener.getAccess().getName().toString());
        }
        catch (Exception e) {
            return "";

        }
    }

    /**
     * This method is used to Retrieves all the excel rows for the respective test case Name(column).
     *
     * @param sExcelFileName Excel file Name
     * @param sSheetName Name of Excel sheet.
     * @param sTestCaseName Excel sheet column value Name
     * @return Hashtable-array with excel column Names and values
     *
     */
    public Hashtable[] getAllExcelRow(String sExcelFileName, String sSheetName, String sTestCaseName){

        Hashtable[] htRow =null;
        boolean bFound =false;


        int iRowCnt = 0, iRowCounter=0;
        String sQuery = "Select * from ["+ sSheetName +"$] where Testcase_ID='"+ sTestCaseName + "' and Execute='Yes'";

        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            ResultSet rsExcelRow = readExcelDatabase (sExcelFileName,sQuery);

            if (rsExcelRow != null){
                ResultSetMetaData rsmd= rsExcelRow.getMetaData();

                int iColumnCount = rsmd.getColumnCount();
                rsExcelRow.beforeFirst();
                while (rsExcelRow.next())
                {
                    if (!bFound){

                        bFound=true;
                        rsExcelRow.last();
                        iRowCnt = rsExcelRow.getRow();

                        rsExcelRow.beforeFirst();

                        htRow = new Hashtable[iRowCnt];

                    }
                    else {
                        Hashtable htTemp = new Hashtable();
                        for (int iColNo=0;iColNo < iColumnCount ;iColNo++ ) {

                            String sVal = rsExcelRow.getString(iColNo+1);
                            if (sVal == null )
                            {
                                htTemp.put(rsmd.getColumnName(iColNo+1),"");
                            }
                            else {
                                htTemp.put(rsmd.getColumnName(iColNo+1),sVal);
                            }

//htTemp.put(rsmd.getColumnName(iColNo + 1), rsExcelRow.getString(iColNo + 1));

                        }
                        htRow[iRowCounter]=htTemp;
                        iRowCounter++;
                    }
                }
            }
            rsExcelRow.close();
        } catch (Exception e) {
            log.error("Exception occurred in {getAllExcelRow} method...");
        }

        return htRow;

    }

    /**
     * This method is used to Retrieves all the excel rows for the respective SQL Query.
     *
     * @param sExcelFileName Excel file Name
     * @param sSQLQuery SQL Query.
     * @return Hashtable-array with excel column Names and values
     *
     */
    public Hashtable[] getAllExcelRow(String sExcelFileName, String sSQLQuery){

        Hashtable[] htRow =null;
        boolean bFound =false;

        int iRowCnt = 0, iRowCounter=0;

        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            ResultSet rsExcelRow = readExcelDatabase (sExcelFileName,sSQLQuery);

            if (rsExcelRow != null){
                ResultSetMetaData rsmd= rsExcelRow.getMetaData();

                int iColumnCount = rsmd.getColumnCount();
                rsExcelRow.beforeFirst();
                while (rsExcelRow.next())
                {
                    if (!bFound){

                        bFound=true;
                        rsExcelRow.last();
                        iRowCnt = rsExcelRow.getRow();

                        rsExcelRow.beforeFirst();

                        htRow = new Hashtable[iRowCnt];

                    }
                    else {
                        Hashtable htTemp = new Hashtable();
                        for (int iColNo=0;iColNo < iColumnCount ;iColNo++ ) {

                            String sVal = rsExcelRow.getString(iColNo+1);
                            if (sVal == null )
                            {
                                htTemp.put(rsmd.getColumnName(iColNo+1),"");
                            }
                            else {
                                htTemp.put(rsmd.getColumnName(iColNo+1),sVal);
                            }

//htTemp.put(rsmd.getColumnName(iColNo + 1), rsExcelRow.getString(iColNo + 1));

                        }
                        htRow[iRowCounter]=htTemp;
                        iRowCounter++;
                    }
                }
            }
            rsExcelRow.close();
        } catch (Exception e) {
            log.error("Exception occurred in {getAllExcelRow} method...");
        }

        return htRow;

    }

    /**
     * This method is used to Retrieves single excel rows for the respective test case Name(column)..
     *
     * @param sExcelFileName Excel file Name
     * @param sSheetName Name of Excel sheet.
     * @param sTestCaseName Excel sheet column value Name
     * @return Hashtable with excel column Names and values
     *
     */
    public Hashtable getExcelRow(String sExcelFileName, String sSheetName, String sTestCaseName){
        Hashtable htRow = new Hashtable();

        String sQuery = "Select * from ["+ sSheetName +"$] where Testcase_ID='"+ sTestCaseName + "'";

        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            ResultSet rsExcelRow = readExcelDatabase(sExcelFileName, sQuery);

            if (rsExcelRow != null){
                ResultSetMetaData rsmd= rsExcelRow.getMetaData();

                int iColumnCount = rsmd.getColumnCount();

                rsExcelRow.beforeFirst();
                if (rsExcelRow.next())
                {
                    for (int iColNo=0;iColNo < iColumnCount ;iColNo++ )
                    {
                        String sVal = rsExcelRow.getString(iColNo+1);
                        if (sVal == null )
                        {
                            htRow.put(rsmd.getColumnName(iColNo+1),"");
                        }
                        else {
                            htRow.put(rsmd.getColumnName(iColNo+1),sVal);
                        }
                    }
                }
            }
            rsExcelRow.close();
        } catch (Exception e) {
            log.error("Error reading test data from "+sExcelFileName + " " + sSheetName);
        }

        return htRow;
    }

    /**
     * This method is used to Read excel file and retrieves respective data return by SQL query.
     *
     * @param sExcelFileName Excel file Name
     * @param sSQLQuery SQL Query Name.
     *
     * @return ResultSet JDBC-ODBC recordset object.
     *
     */
    public ResultSet readExcelDatabase(String sExcelFileName, String sSQLQuery) {
        Connection c = null;
        Statement stmnt = null;
        ResultSet rsData = null;
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            if (this.isFileExists(sExcelFileName) == false){
                log.error("File <" + sExcelFileName + "> does not exists...");
                return rsData;
            }
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=" + sExcelFileName);
            c.getClientInfo();
            stmnt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

//String query = sSQLQuery; //"Select * from [Sheet1$]" ;
            ResultSet rs = stmnt.executeQuery(sSQLQuery );
            rsData = rs;


        } catch (Exception e) {
            log.error("Error reading excel database " + sExcelFileName);
        }
        return rsData;
    }


    /**
     * This method is used to Insert or update excel file record provided in SQL query.
     *
     * @param sExcelFileName Excel file Name
     * @param sSQLQuery SQL Query Name.
     *
     */
    public void writeExcelDatabase(String sExcelFileName, String sSQLQuery){
        Connection c = null;
        Statement stmnt = null;
        ResultSet rsData = null;
        try
        {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
            c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=" + sExcelFileName+";readOnly=false");
            stmnt = c.createStatement();
            stmnt.executeUpdate( sSQLQuery );

        }

        catch( Exception e )
        {
            log.error("Error writing excel database " + sExcelFileName);
        }
    }

    /**
     * This method is used to Update Test1 Case execution status in Result Summary file.
     *
     * @param result ITestResult object
     *
     */
    public void updateTestExecutionResult_old(ITestResult result){

        String sExcelFileName = EnvSetup.TEST_EXECUTION_RESULT_FILE;
        Connection cConnection = null;
        Statement stmnt = null;
        ResultSet rsData = null;
        String sQuery = "";
        String sSnapFileName = "";
        try
        {

            String sTestCaseName = result.getTestContext().getCurrentXmlTest().getName();

            String sScreenShotName = sTestCaseName;
            sScreenShotName = sScreenShotName.replaceAll("'","");
            sScreenShotName = sScreenShotName.replaceAll("\\*","_");

            sTestCaseName = sTestCaseName.replaceAll("'","''");

            String sStatus = "";
            String sErrorMsg = "";

            switch (result.getStatus())
            {
                case ITestResult.SUCCESS: sStatus = "Passed";
                    sErrorMsg = "-";
                    break;
                case ITestResult.FAILURE: sStatus = "Failed";
//sSnapFileName = takeDesktopScreenShot(EnvSetup.SNAP_FOLDER_PATH, sScreenShotName );
                    sSnapFileName = takeWebPageScreenShot(EnvSetup.WEBDRIVER,EnvSetup.SNAP_FOLDER_PATH, sScreenShotName );
                    sErrorMsg = "Please refer error snapshot name - " + sSnapFileName;
                    break;
                case ITestResult.SKIP: sStatus = "Skipped";
//sSnapFileName = takeDesktopScreenShot(EnvSetup.SNAP_FOLDER_PATH, sScreenShotName);
                    sSnapFileName = takeWebPageScreenShot(EnvSetup.WEBDRIVER,EnvSetup.SNAP_FOLDER_PATH, sScreenShotName );
                    sErrorMsg = "Please refer error snapshot name - " + sSnapFileName;
                    break;
                default:
                    sStatus = "No Status";
                    sErrorMsg = "-";
                    break;
            }
            Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
            cConnection = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=" + sExcelFileName+";readOnly=false");

            stmnt = cConnection.createStatement();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");

            Date date = new Date();

            String[] sArrTCNames = sTestCaseName.split("\\*");
            for(String sTCName: sArrTCNames){
                if (ITestResult.STARTED == result.getStatus()) {
                    Hashtable hData = EnvSetup.hTestDetails;
                    String sFEATName = "-";
                    if (hData != null) {
                        if (hData.isEmpty() == false && hData.containsKey("FEAT") == true){
                            if (hData.get("FEAT").toString().trim() != "") {
                                sFEATName = hData.get("FEAT").toString();
                            }
                        }
                    }
                    sQuery = "INSERT INTO [Sheet1$] VALUES ('" +sTCName+"','"+ dateFormat.format(date)+"','-','-','"+sStatus+"','"+sErrorMsg+"','"+ sFEATName+"')";
                    stmnt.executeUpdate( sQuery );
                    EnvSetup.TESTCASE_START_TIME = dateFormat.format(date);
                    EnvSetup.TESTCASE_FEAT = sFEATName;
                }else {

//update test case result in Excel File

                    sQuery = "Update [Sheet1$] set ExecutionTime=CStr(format(CDate(ExecutionStartDate)-CDate('"+dateFormat.format(date)+"'),'hh:mm:ss')) ,Status='"+sStatus+"',ErrorMessage='"+sErrorMsg+"',ExecutionEndDate='"+ dateFormat.format(date)+"' where TestCaseName='" +sTCName+"' and Status='No Status'";
//sQuery = "Update [Sheet1$] set ExecutionTime=CStr(format(CDate(ExecutionStartDate)-CDate('"+dateFormat.format(date)+"'),'hh:mm:ss')) ,Status='"+sStatus+"',ErrorMessage='"+sErrorMsg+"',ExecutionEndDate='"+ dateFormat.format(date)+"' where TestCaseName='" +sTCName+"' and Status='No Status'";
                    stmnt.executeUpdate( sQuery );

                    String sEdate = dateFormat.format(date);

                    Date d1 = null,d2=null;
                    d1 = dateFormat.parse(EnvSetup.TESTCASE_START_TIME);
                    d2 = dateFormat.parse(sEdate);

                    long diff = Math.abs(d1.getTime()-d2.getTime());

//long diffSeconds = diff / 1000 % 60;
//long diffMinutes = diff / (60 * 1000) % 60;
//long diffHours = diff / (60 * 60 * 1000) % 24;

                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    df.setTimeZone(tz);
                    String diffEtime = df.format(new Date(diff));


//String sData = sTCName + ","+ EnvSetup.TESTCASE_START_TIME + "," + dateFormat.format(date) + ","+diffHours + ":" + diffMinutes+":" + diffSeconds +"," + sStatus + "," + sErrorMsg+ ","+EnvSetup.TESTCASE_FEAT ;
                    String sData = sTCName + ","+ EnvSetup.TESTCASE_START_TIME + "," + dateFormat.format(date) + ","+ diffEtime +"," + sStatus + "," + sErrorMsg+ ","+EnvSetup.TESTCASE_FEAT ;
                    String filename= EnvSetup.TEST_EXECUTION_CSV_RESULT_FILE;
                    FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                    fw.write(sData+"\n");//appends the string to the file
                    fw.close();
                }
            }

            stmnt.close();
            cConnection.close();
        }
        catch( Exception e )
        {
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
        }

    }

    /**
     * This method will update Test execution results in test results CSV file.
     * If test cases fails it will create snapshot file containing screenshot.
     * @param result
     */
    public void updateTestExecutionResult(ITestResult result){

        String snapFileName,status,errorMsg,bambooURL,jiraIDs = "";
        String link = "-";
        int testCount;

        try
        {

            testCount = getTestCountFromFile(result);
            String sTestCaseName = result.getTestContext().getCurrentXmlTest().getName();
            String sScreenShotName = "" + (testCount + 1); //sTestCaseName;

//sTestCaseName = sTestCaseName.replaceAll("'","''");

            switch (result.getStatus())
            {
                case ITestResult.SUCCESS:
                    status = "<p style='background-color:green'>Passed</p>";
                    errorMsg = "-";
                    break;
                case ITestResult.FAILURE:
                    status = "<p style='background-color:red'>Failed</p>";
                    snapFileName = takeWebPageScreenShot(EnvSetup.WEBDRIVER,EnvSetup.SNAP_FOLDER_PATH, sScreenShotName );
                    errorMsg = "Please refer error snapshot name - " + snapFileName;
                    break;
                case ITestResult.SKIP:
                    status = "<p style='background-color:yellow'>Skipped</p>";
                    errorMsg = "-";
                    break;
                default:
                    status = "No Status";
                    errorMsg = "-";
                    break;
            }

            if (EnvSetup.TESTCASE_FEAT.equalsIgnoreCase("PRE-REQUISITE") || EnvSetup.TESTCASE_FEAT.equalsIgnoreCase("Prerequisite")) {
                return;
            }

            jiraIDs=EnvSetup.TEST_JIRA_ID;

            String[] sArrTCNames = sTestCaseName.split("\\*");
            String[] testIDs=jiraIDs.split("\\*");

// Counter for mapping JIRA IDs to Test cases.
            int i=0;

            for(String sTCName: sArrTCNames) {

                try {
                    if (!jiraIDs.equals("-")) {
                        link = "<a href=" + "\"" + EnvSetup.BAMBOO_URL + testIDs[i] + "\">" + testIDs[i] + "</a>";
                    }
                } catch (ArrayIndexOutOfBoundsException a)
                {
                    link="-";
                }
                testCount = testCount+1;
                updateTestCountFile(testCount);

// Update CSV file
                updateCSVFile(testCount,link,sTCName,status,errorMsg);
                i++;
            }
        }
        catch( Exception e )
        {
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
        }

    }

    /***
     *
     * @param testCount
     * @param testCaseName
     * @param status
     * @param errorMessage
     */
    public void updateCSVFile(int testCount,String link,String testCaseName,String status, String errorMessage )
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            Date date = new Date();
            String sEdate = dateFormat.format(date);

            Date d1 = null,d2=null;
            d1 = dateFormat.parse(EnvSetup.TESTCASE_START_TIME);
            d2 = dateFormat.parse(sEdate);

            long diff = Math.abs(d1.getTime()-d2.getTime());

            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(tz);
            String diffEtime = df.format(new Date(diff));

            if (!isFileExists(EnvSetup.TEST_EXECUTION_CSV_RESULT_FILE)) {
                FileWriter fw = new FileWriter(EnvSetup.TEST_EXECUTION_CSV_RESULT_FILE, true); //the true will append the new data
                fw.append("SrNo,JIRA ID,Test Case Name,Start Time,End Time,Execution Time,Status,Error,FEAT");
                fw.append("\r\n");
                fw.close();
            }
            String sData = "" + (testCount) + "," + link + "," + testCaseName + "," + EnvSetup.TESTCASE_START_TIME + "," + dateFormat.format(date) + "," + diffEtime + "," + status + "," + errorMessage + "," + EnvSetup.TESTCASE_FEAT;
            FileWriter fw = new FileWriter(EnvSetup.TEST_EXECUTION_CSV_RESULT_FILE, true); //the true will append the new data
            fw.append(sData);//appends the string to the file
            fw.append("\r\n");
            fw.close();
        }
        catch( Exception e )
        {
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    /***
     *
     * @param result
     * @return : Test Count as integer.
     */
    public int getTestCountFromFile(ITestResult result)
    {
        int iTestCnt = 0;
        try {
            if (isFileExists(EnvSetup.TEST_EXECUTION_COUNT_FILE)) {
                FileInputStream inputStream = new FileInputStream(EnvSetup.TEST_EXECUTION_COUNT_FILE);
                iTestCnt = Integer.parseInt(IOUtils.toString(inputStream));
            }
        }
        catch( Exception e )
        {
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        return iTestCnt;
    }

    /***
     *
     * @param testCount
     */
    public void updateTestCountFile(int testCount)
    {
        try {
            FileWriter fw1 = new FileWriter(EnvSetup.TEST_EXECUTION_COUNT_FILE); //the true will append the new data
            fw1.write(Integer.toString(testCount ));
            fw1.close();
        }
        catch( Exception e )
        {
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

// HTML Table functions

    /**
     * This method is used to get HTML table details in Hashtable.
     *
     * @param table Html WebTable object
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return HashTable[] To get list of HTML rows data in the form of HashTable.
     *
     */
    public Hashtable[] getTableData(WebElement table, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

// Now get all the TR elements from the table
            List<WebElement> allRows = table.findElements(By.tagName("tr"));

            int iAddRowNo = 1;

            if (bHeaderExits == false){
                iAddRowNo = 0;
            }

            Hashtable[] htData = null;
            if (allRows.size() > iAddRowNo ) {
                boolean bHeader=true;
                Hashtable htHeader =new Hashtable();

                htData =new Hashtable[allRows.size()-iAddRowNo];
                int iCount = 0, iRowCounter=0;
// And iterate over them, getting the cells
                for (WebElement row : allRows) {
//Get Column Header details
                    if ( bHeader ){
                        bHeader=false;
                        iCount = 1;
                        Hashtable htTemp = new Hashtable();
                        List<WebElement> cells = row.findElements(By.tagName("th"));
                        if (cells.size() == 0){
                            cells = row.findElements(By.tagName("td"));
                        }
                        for (WebElement cell : cells) {
                            if (bHeaderExits == false){
                                htHeader.put("Column"+iCount,"Column"+iCount);
                                htTemp.put("Column"+iCount, cell.getText());
                            }
                            else {
                                htHeader.put("Column"+iCount,cell.getText().trim());
                            }
                            iCount++;
                        }
                        if (bHeaderExits == false){
                            htData[iRowCounter] = htTemp;
                            iRowCounter++;
                        }

                    }
                    else
                    {
                        Hashtable htTemp = new Hashtable();

                        List<WebElement> cells = row.findElements(By.tagName("td"));
                        iCount=1;
                        for (WebElement cell : cells) {
                            htTemp.put(htHeader.get("Column"+iCount), cell.getText().trim());
                            iCount++;
                        }
                        htData[iRowCounter] = htTemp;
                        iRowCounter++;
                    }
                }
            }
            return htData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return null;
        }
    }

    /**
     * This method is used to verify user specified data with actual available data in HTML table.
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return boolean return true if data matched otherwise false.
     *
     */
    public boolean lookupTableData(WebElement table, String sData, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            Hashtable hRowData = getTableRowData(table,sData,bHeaderExits);

            if (hRowData == null){
                return false;
            }
            return true;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     This method is used to get HTML table row detail in Hashtable.
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return HashTable - Return matched HTML rows data in the form of HashTable.
     *
     */
    public Hashtable getTableRowData(WebElement table, String sData, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            Hashtable[] htData = getTableData(table,bHeaderExits);
            boolean bFlag = false;
            Hashtable hRowData = null;
            if (htData != null){
                int iCount = 0;
                String[] sArrData = sData.split(";");
                String[][] sArrColVal = new String[sArrData.length][2];
                for (String sTemp:sArrData){
                    sArrColVal[iCount][0] = sTemp.split("=")[0];
                    sArrColVal[iCount][1] = sTemp.split("=")[1].trim();
                    iCount++;
                }

                for (Hashtable hTemp: htData){
                    bFlag = true;
                    for (iCount=0;iCount<sArrData.length;iCount++){
                        if (!hTemp.get(sArrColVal[iCount][0]).toString().equals(sArrColVal[iCount][1])){
                            bFlag = false;
                            break;
                        }
                    }
                    if (bFlag){
                        hRowData = hTemp;
                        break;
                    }
                }
            }
            return hRowData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return null;
        }
    }

    /**
     This method is used to select the radio button or tick the checkbox in HTML table row .
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bCheckNode Check/un-check the check box based on the value. Ignore this parameter value for Radio button.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return HashTable - Return matched HTML rows data in the form of HashTable.
     *
     */
    public boolean selectTableRow(WebElement table, String sData, boolean bCheckNode, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            WebElement tableRow = getTableRow (table,sData,bHeaderExits);

            if (tableRow != null){
                List<WebElement> inputList = tableRow.findElements(By.tagName("input"));

                for (WebElement chkbox:inputList){

                    if (chkbox.getAttribute("type").equalsIgnoreCase("checkbox") || chkbox.getAttribute("type").equalsIgnoreCase("radio")){
                        if (bCheckNode){
                            if (!chkbox.isSelected()){
                                chkbox.click();
                            }
                        }
                        else {
                            if (chkbox.isSelected()){
                                if(chkbox.getAttribute("type").toLowerCase().equals("checkbox")){
                                    chkbox.click();
                                }
// else {
// if(chkbox.getAttribute("type").toLowerCase().equals("radio")){
// chkbox.clear();
// }
// }
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     *
     * @param driver WebDriver to select table Row checkbox via Javascript
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bCheckNode Check/un-check the check box based on the value. Ignore this parameter value for Radio button.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return HashTable - Return matched HTML rows data in the form of HashTable.
     *
     */
    public boolean selectTableRow(WebDriver driver, WebElement table, String sData, boolean bCheckNode, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            WebElement tableRow = getTableRow (table,sData,bHeaderExits);

            if (tableRow != null){
                List<WebElement> inputList = tableRow.findElements(By.tagName("input"));

                for (WebElement chkbox:inputList){

                    if (chkbox.getAttribute("type").equalsIgnoreCase("checkbox") || chkbox.getAttribute("type").equalsIgnoreCase("radio")){
                        boolean bAction = false;
                        if (bCheckNode){
                            if (!chkbox.isSelected()){
                                bAction =true;
                            }
                        }
                        else {
                            if (chkbox.isSelected()){
                                if(chkbox.getAttribute("type").toLowerCase().equals("checkbox")){
                                    bAction =true;
                                }
                            }
                        }

                        if (bAction) {
                            JavascriptExecutor executor1 = (JavascriptExecutor)driver;
                            executor1.executeScript("arguments[0].click();", chkbox);
                        }
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     This method is used to verify if the radio button or checkbox in HTML table row is selected or not.
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return boolean - Return true if row checkbox or radio button selected else false.
     *
     */
    public boolean lookupTableRowSelected(WebElement table, String sData, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            WebElement tableRow = getTableRow (table,sData,bHeaderExits);

            if (tableRow != null){
                List<WebElement> inputList = tableRow.findElements(By.tagName("input"));

                for (WebElement chkbox:inputList){

                    if (chkbox.getAttribute("type").equalsIgnoreCase("checkbox") || chkbox.getAttribute("type").equalsIgnoreCase("radio")) {

                        if (chkbox.isSelected()) {
                            return true;
                        }

                        return false;
                    }
                }
                return false;
            }
            return false;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     This method is used to select the radio button or tick the checkbox in HTML table row .
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return HashTable - Return matched HTML rows data in the form of HashTable.
     *
     */
    public boolean clickTableCell(WebElement table, String sData, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            WebElement tableRow = getTableRow (table,sData,bHeaderExits);

            String sColValue = sData.split(";")[0].split("=")[1].trim();

            if (tableRow != null){
                boolean bFlag= false;
                List<WebElement> inputList = tableRow.findElements(By.tagName("a"));

                if (inputList.size() > 0){

                    for (WebElement cell:inputList){
                        if (cell.getText().contains(sColValue)){
                            cell.click();
                            return true;
                        }

                    }
                }
                else {
                    inputList = tableRow.findElements(By.tagName("td"));
                    if (inputList.size() > 0){
                        for (WebElement cell:inputList){
                            if (cell.getText().contains(sColValue)){
                                cell.click();
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     This method is used to get HTML Table row number.
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return int - Return matched HTML row number.
     *
     */
    public int getTableRowNum(WebElement table, String sData, boolean bHeaderExits){
        try{
            int iTableRow=-1;

            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            Hashtable[] htData = getTableData(table, bHeaderExits);

            if (htData != null){
                boolean bFlag = false;
// Now get all the TR elements from the table
                List<WebElement> allRows = table.findElements(By.tagName("tr"));

                int iCount = 0;
                String[] sArrData = sData.split(";");
                String[][] sArrColVal = new String[sArrData.length][2];
                for (String sTemp:sArrData){
                    sArrColVal[iCount][0] = sTemp.split("=")[0];
                    sArrColVal[iCount][1] = sTemp.split("=")[1].trim();
                    iCount++;
                }

//for (Hashtable hTemp: htData){
                for (int iHTCount=0;iHTCount<htData.length;iHTCount++){
                    bFlag = true;
                    Hashtable hTemp = htData[iHTCount];

                    for (int iCCount = 0;iCCount < sArrData.length; iCCount++){
                        if (!hTemp.get(sArrColVal[iCCount][0]).toString().equals(sArrColVal[iCCount][1])){
                            bFlag = false;
                            break;
                        }
                    }
                    if (bFlag){
                        int iHeaderNo=2;
                        if (!bHeaderExits){
                            iHeaderNo = 1;
                        }
                        iTableRow = iHTCount+iHeaderNo;
                        break;
                    }
                }
            }
            return iTableRow;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return -1;
        }
    }

    /**
     This method is used to get HTML Table row.
     *
     * @param table Html WebTable object
     * @param sData User defined search data in the form of column and value. e.g. ColumnName=ColumnValue. multiple value can be separated via semicolon (;) operator.
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     * @return HashTable - Return matched HTML rows data in the form of HashTable.
     *
     */
    public WebElement getTableRow(WebElement table, String sData, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            WebElement tableRow = null;

            Hashtable[] htData = getTableData(table,bHeaderExits);

            if (htData != null){
                boolean bFlag = false;
// Now get all the TR elements from the table
                List<WebElement> allRows = table.findElements(By.tagName("tr"));

                int iCount = 0;
                String[] sArrData = sData.split(";");
                String[][] sArrColVal = new String[sArrData.length][2];
                for (String sTemp:sArrData){
                    sArrColVal[iCount][0] = sTemp.split("=")[0];
                    sArrColVal[iCount][1] = sTemp.split("=")[1].trim();
                    iCount++;
                }

//for (Hashtable hTemp: htData){
                for (int iHTCount=0;iHTCount<htData.length;iHTCount++){
                    bFlag = true;
                    Hashtable hTemp = htData[iHTCount];
                    for (int iCCount=0;iCCount<sArrData.length;iCCount++){
                        if (!hTemp.get(sArrColVal[iCCount][0]).toString().equals(sArrColVal[iCCount][1])){
                            bFlag = false;
                            break;
                        }
                    }
                    if (bFlag){
                        int iHeaderNo=1;
                        if (!bHeaderExits){
                            iHeaderNo = 0;
                        }
                        tableRow = allRows.get(iHTCount+iHeaderNo);

                        break;
                    }
                }
            }
            return tableRow;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return null;
        }
    }

    /**
     This method is used to get total row Count of specified HTML table.
     *
     * @param table Html WebTable object
     * @param bHeaderExits Pass the value as "true" if HTML table has header otherwise false
     *
     * @return int - Total number of row(s). Return -1 if exception occurs.
     *
     */
    public int getTableRowCount(WebElement table, boolean bHeaderExits){
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            Hashtable[] hData = getTableData(table,bHeaderExits);

            if (hData != null){
                return hData.length;
            }
            return 0;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return -1;
        }
    }


    /**
     This method is used to check if current test case to skip or not.
     *
     * *@param iTestResult ITestResult Object
     * @param hData hashtable Object
     *
     * @return boolean - return true if user has marked Execute status as "No" else return false.
     *
     */
    public boolean checkIfTestToSkip(Hashtable hData) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            if (hData != null) {
                log.debug("Test case is not null ");
                if (hData.isEmpty() == false && hData.containsKey("Execute") == true){
                    log.debug("Test case is not empty ");
                    if (hData.get("Execute").toString().toLowerCase().equals("no")){
                        log.debug("Execute Status is No");
//throw new SkipException("Skipping testcase : <" + sTestCaseName + "> by user");
                        return true;
                    }
                }
            }
            return false;
        }
        catch (Exception e){

            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /**
     This method is used to get Test Case details in the form of Hashtable.
     *
     *
     * @return Hashtable - return test case details.
     *
     */
    public Hashtable getTestDetails(){
        Hashtable hData = new Hashtable();
        try{

            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

//Get Excel File Name
//String sExcelFileName = SuiteListener.getAccess().getParameter("dataFile");
            String sExcelFileName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("dataFile");
            if (sExcelFileName == null){
//sExcelFileName = SuiteListener.getAccess().getName().toString().trim()+".xls";
                log.error("Input [dataFile] parameter is missing for Test Case -[" + Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name - [" + SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

//Get Excel Data Sheet File Name
            String sSheetName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("dataSheet");
            if (sSheetName == null){
//sSheetName = Reporter.getCurrentTestResult().getInstanceName().toString().trim();
                log.error("Specified [dataSheet] parameter is missing for Test Case -[" + Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name: ["+ SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

            String[] sArrClassName= sSheetName.split("\\.");
            sSheetName = sArrClassName[sArrClassName.length-1];

//Get Test Case Name from Suite xml file
            String sTestCaseName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim();

            sTestCaseName = sTestCaseName.replaceAll("'","''");

            hData = this.getExcelRow(EnvSetup.TEST_INPUT_DATA_PATH+sExcelFileName,sSheetName,sTestCaseName);

            return hData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return hData;
        }
    }

    /**
     This method is used to get Test Case details in the form of Hashtable.
     *
     * @param sSheetName Data Sheet Name containing test case details
     * @return Hashtable - return test case details.
     *
     */
    public Hashtable getTestDetails(String sSheetName){
        Hashtable hData = new Hashtable();
        try{

            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

//Get Excel File Name
//String sExcelFileName = SuiteListener.getAccess().getParameter("dataFile");
            String sExcelFileName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("dataFile");

            if (sExcelFileName == null){
//sExcelFileName = SuiteListener.getAccess().getName().toString().trim()+".xls";
                log.error("Input [dataFile] parameter is missing for Test Case -[" + Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name - [" + SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

//Get Test Case Name from Suite xml file
            String sTestCaseName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim();

            sTestCaseName = sTestCaseName.replaceAll("'","''");

            hData = this.getExcelRow(EnvSetup.TEST_INPUT_DATA_PATH+sExcelFileName,sSheetName,sTestCaseName);

            return hData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return hData;
        }
    }

    /**
     This method is used to get Test Case details in the form of Hashtable.
     *
     * @param sExcelFileName Name of the Excel data file
     * @param sSheetName Data Sheet Name containing test case details
     * @return Hashtable - return test case details.
     *
     */
    public Hashtable getTestDetails(String sExcelFileName, String sSheetName){
        Hashtable hData = new Hashtable();
        try{

            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

//Get Test Case Name from Suite xml file
            String sTestCaseName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim();

            sTestCaseName = sTestCaseName.replaceAll("'","''");

            hData = this.getExcelRow(EnvSetup.TEST_INPUT_DATA_PATH+sExcelFileName,sSheetName,sTestCaseName);

            return hData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return hData;
        }
    }

    /**
     This method is used to get Test Case details in the form of Hashtable.
     *
     * @param iTestResult - ITestResult object
     * @return Hashtable - return test case details.
     *
     */
    public Hashtable getTestDetails(ITestResult iTestResult){
        Hashtable hData = new Hashtable();
        try{

            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

//Get Excel File Name
//String sExcelFileName = SuiteListener.getAccess().getParameter("dataFile");
            String sExcelFileName = iTestResult.getTestContext().getCurrentXmlTest().getTestParameters().get("dataFile");

            if (sExcelFileName == null){
//sExcelFileName = SuiteListener.getAccess().getName().toString().trim()+".xls";
                log.error("Input file parameter is missing for Test Case -[" + iTestResult.getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name - [" + SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }
            if (!isFileExists(EnvSetup.TEST_INPUT_DATA_PATH+sExcelFileName)) {
                log.error("Specified Input file [" + sExcelFileName + "] does not exists for Test Case -[" + iTestResult.getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name: ["+ SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

//Get Excel Data Sheet File Name
            String sSheetName = iTestResult.getTestContext().getCurrentXmlTest().getTestParameters().get("dataSheet");
            if (sSheetName == null){
//sSheetName = iTestResult.getInstanceName().toString().trim();
                log.error("Specified [dataSheet] parameter is missing for Test Case -[" + iTestResult.getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name: ["+ SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

            String[] sArrClassName= sSheetName.split("\\.");
            sSheetName = sArrClassName[sArrClassName.length-1];

//Get Test Case Name from Suite xml file
            String sTestCaseName = iTestResult.getTestContext().getCurrentXmlTest().getName().toString().trim();

            sTestCaseName = sTestCaseName.replaceAll("'","''");


            if (EnvSetup.APP_LANGUAGE.equalsIgnoreCase("en_us")) {
                hData = this.getExcelRow(EnvSetup.TEST_INPUT_DATA_PATH+sExcelFileName,sSheetName,sTestCaseName);
            }else {
                deleteFile(EnvSetup.SNAP_FOLDER_PATH + "dump.csv");
                this.wait(1);
                convertXLS2CSV(EnvSetup.TEST_INPUT_DATA_PATH + sExcelFileName,sSheetName,EnvSetup.SNAP_FOLDER_PATH + "dump.csv");
                Hashtable[] hTData= getCSVRows(EnvSetup.SNAP_FOLDER_PATH + "dump.csv");
                hTData = getFilterData(hTData,"Testcase_ID",sTestCaseName);

                if (hTData!=null) {
                    hData = hTData[0];
                }
            }



            return hData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return hData;
        }
    }

    /**
     This method is used to get Multiple excel row for Test Case details in the form of Hashtable array.
     *
     *
     * @return Hashtable[] - return test case details.
     *
     */
    public Hashtable[] getAllTestDetails(){
        Hashtable[] hData = null ;
        try{

            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

//Get Excel File Name
//String sExcelFileName = SuiteListener.getAccess().getParameter("dataFile");
            String sExcelFileName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("dataFile");

            if (sExcelFileName == null){
//sExcelFileName = SuiteListener.getAccess().getName().toString().trim()+".xls";
                log.error("Input [dataFile] parameter is missing for Test Case -[" + Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name - [" + SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

//Get Excel Data Sheet File Name
            String sSheetName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getTestParameters().get("dataSheet");
            if (sSheetName == null){
//sSheetName = Reporter.getCurrentTestResult().getInstanceName().toString().trim();
                log.error("Specified [dataSheet] parameter is missing for Test Case -[" + Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim()+"]. Suite Name: ["+ SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

            String[] sArrClassName= sSheetName.split("\\.");
            sSheetName = sArrClassName[sArrClassName.length-1];

//Get Test Case Name from Suite xml file
            String sTestCaseName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getName().toString().trim();

            sTestCaseName = sTestCaseName.replaceAll("'","''");

            hData = this.getAllExcelRow(EnvSetup.TEST_INPUT_DATA_PATH+sExcelFileName,sSheetName,sTestCaseName);

            return hData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return hData;
        }
    }

    /**
     This method is used to get Multiple excel row for Test Case details in the form of Hashtable array.
     *
     * @param context - ITestContext object.
     * @return Hashtable[] - return test case details.
     *
     */
    public Hashtable[] getAllTestDetails(ITestContext context){
        Hashtable[] hData = null ;
        try{
            ITestResult iTestResult;
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

//Get Excel File Name
//String sExcelFileName = SuiteListener.getAccess().getParameter("dataFile");
            String sExcelFileName = context.getCurrentXmlTest().getTestParameters().get("dataFile");

            if (sExcelFileName == null){
//sExcelFileName = SuiteListener.getAccess().getName().toString().trim()+".xls";
                log.error("Input [dataFile] parameter is missing for Test Case -[" + context.getCurrentXmlTest().getName().toString().trim()+"]. Suite Name - [" + SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

//Get Excel Data Sheet File Name
            String sSheetName = context.getCurrentXmlTest().getTestParameters().get("dataSheet");
            if (sSheetName == null){
//sSheetName = context.getCurrentXmlTest().getXmlClasses().get(0).getName().toString();
                log.error("Specified [dataSheet] parameter is missing for Test Case -[" + context.getCurrentXmlTest().getName().toString().trim()+"]. Suite Name: ["+ SuiteListener.getAccess().getName().toString() +"]");
                return hData;
            }

            String[] sArrClassName= sSheetName.split("\\.");
            sSheetName = sArrClassName[sArrClassName.length-1];

//Get Test Case Name from Suite xml file
            String sTestCaseName = context.getCurrentXmlTest().getName().toString().trim();

            sTestCaseName = sTestCaseName.replaceAll("'","''");

            if (EnvSetup.APP_LANGUAGE.equalsIgnoreCase("en_us")) {
                hData = this.getAllExcelRow(EnvSetup.TEST_INPUT_DATA_PATH + sExcelFileName, sSheetName, sTestCaseName);
            }else {
                deleteFile(EnvSetup.SNAP_FOLDER_PATH + "dump1.csv");
                this.wait(1);
                convertXLS2CSV(EnvSetup.TEST_INPUT_DATA_PATH + sExcelFileName,sSheetName,EnvSetup.SNAP_FOLDER_PATH + "dump1.csv");
                hData= getCSVRows(EnvSetup.SNAP_FOLDER_PATH + "dump1.csv");
                hData = getFilterData(hData,"Testcase_ID",sTestCaseName);
            }
            return hData;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return hData;
        }
    }


    /**
     This method is used to get selected dropdown option.
     *
     * @param driver Selenium WebDriver object
     * @param element Dropdown/Combo box object
     * @return String - returns selected value from dropdown .
     *
     */
    public String getDropDownOption(WebDriver driver, WebElement element) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        try {
// GetText from the drop down list
            String selectedOption = new Select(element).getFirstSelectedOption().getText();
// return selection option
            return (selectedOption);
        }catch(NoSuchElementException e)
        {
            return e.getMessage();
        }
        catch(Exception e)
        {
            log.error("Exception while getting first selected item of the combobox");
            return null;
        }
    }

    /**
     This method is used to select value from visible Text from dropdown list.
     *
     * @param driver Selenium WebDriver object
     * @param element Dropdown/Combo box object
     * @param sItem Item to select from dropdown
     *
     * @return String - returns selected value from dropdown .
     *
     */
    public boolean selectDropDownOption(WebDriver driver, WebElement element,String sItem) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try{
            Select selectedOption = new Select(element);
            this.wait(4);
//selectedOption.selectByValue(sItem);
            selectedOption.selectByVisibleText(sItem);
            log.info("Selected option "+sItem);

            return true;
        } catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            log.error("Error while selecting:"+sItem + ": ERROR "+e.toString());
            return false;
        }

    }


    /**
     This method is used to select dropdown by Index from dropdown list.
     *
     * @param driver Selenium WebDriver object
     * @param element Dropdown/Combo box object
     * @param iIndex Item to select from dropdown
     *
     * @return String - returns selected value from dropdown .
     *
     */
    public boolean selectDropDownOptionByIndex(WebDriver driver, WebElement element,int iIndex) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try{
            Select selectedOption = new Select(element);


            selectedOption.selectByIndex(iIndex);
//selectedOption.selectByVisibleText(sItem);


            return true;
        } catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }

    }

    /**
     This method is used to select value from dropdown list.
     *
     * @param driver Selenium WebDriver object
     * @param element Dropdown/Combo box object
     * @param sItem Item to select value from dropdown
     *
     * @return String - returns selected value from dropdown .
     *
     */
    public boolean selectDropDownOptionByValue(WebDriver driver, WebElement element,String sItem) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try{
            Select selectedOption = new Select(element);
            this.wait(4);
            selectedOption.selectByValue(sItem);

            log.info("Selected option value "+sItem);

            return true;
        } catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            log.error("Error while selecting value:"+sItem + ": ERROR "+e.toString());
            return false;
        }

    }

    /**
     * This method is used to get all available values from dropdown list.
     *
     * @param driver Selenium WebDriver object
     * @param element Dropdown/Combo box object
     * @return List<String> Return available items in Combo/Dropdown box.
     */
    public List<String> getDropdownValues(WebDriver driver, WebElement element) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        List<String> lstOptions = new ArrayList<String>();
        try{
            Select selectedOption = new Select(element);

            List<WebElement> lstOpt = selectedOption.getOptions();

            for (WebElement ele : lstOpt) {
                lstOptions.add(ele.getText().toString());
            }

            return lstOptions;
        } catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return lstOptions;
        }

    }

    /**
     This method handles certificate screen.
     *
     * @param driver Selenium WebDriver object
     * @param BrowserName Name of the browser. e.g FIREFOX/IE/CHROME/SAFARI
     */
    public void setBrowserSSLCertificate(WebDriver driver,String BrowserName){

        if (BrowserName.equalsIgnoreCase("ie")){
            if (driver.getTitle().toLowerCase().contains("certificate error:")){
                driver.navigate().to("javascript:document.getElementById('overridelink').click()");
            }
        }else if(BrowserName.equalsIgnoreCase("safari")){
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ENTER);
            }
            catch (Exception e) {}

        }else if(BrowserName.equalsIgnoreCase("chrome")){

        }else if(BrowserName.equalsIgnoreCase("firefox")){

        }

    }

    /**
     This method is used to launch the suite specific browser for execution.
     *
     */
    public void suiteBrowserDetails(){
//Get Suite Browser Name
        String sSuiteBrowser = SuiteListener.getAccess().getParameter("browser");

        if (sSuiteBrowser == null ) {
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

    /**
     This method is used to get list of all options from Context Menu object.
     *
     * @param driver Selenium WebDriver object
     * @param sClassName - The Class name of the context Menu Options
     * @return List<String> - Returns list of all context menu options.
     *
     */
    public List<String> getContextMenuOptions(WebDriver driver,String sClassName) {
        List<String> lsOptions = new ArrayList<String>();
// Preserve list of context menu options web elements
        List<WebElement> Optionset = driver.findElements(By.className(sClassName));
        log.debug("Optionset.size() "+Optionset.size());
// Iterate through list of Options.
        for (WebElement Option : Optionset ){
            log.debug("Option.getAttribute "+ Option.getAttribute("title").toString());
            lsOptions.add(Option.getAttribute("title").toString());
        }
        return lsOptions;
    }

    /**
     This method is used to execute same Test for multiple or different data.
     *
     * @param context ITestContext global variable.
     * @return Hashtable array
     * #@Test(dataProviderClass = UtilityFun.class, dataProvider = "getDataProviderDetails")
     * # Add above line to test/method where user want to do data driven.
     *
     */
    @DataProvider (name = "getDataProviderDetails")
    public static Hashtable[][] getDataProviderDetails(ITestContext context) {
        UtilityFun objFun = new UtilityFun();
        Hashtable[] hData = objFun.getAllTestDetails(context);
        Hashtable[][] result = null;
        if (hData != null ) {
            result = new Hashtable[hData.length][1];
            int iCnt=0;

            for (Hashtable h: hData) {
                result[iCnt][0]=h;
                iCnt++;
            }
        } else {
            result = new Hashtable[1][1];
            result[0][0] = null;

        }

        return result;
    }

    /**
     This method is used to execute command on remote machine and return execution status code.
     *
     * @param sHostName Name of the linux machine on which script/command gets executed.
     * @param sUserName user name of host machine
     * @param sPassword password of host machine
     * @param sCommandToRun Command to be execute on target/host machine
     *
     * @return Hashtable return executecode and output.
     */
    public Hashtable ssh_Execute_Command (String sHostName, String sUserName, String sPassword, String sCommandToRun) {
        Hashtable hSSH = new Hashtable();
        SSHExecute ssh = new SSHExecute();
        ssh.connect(sHostName,sUserName,sPassword);
        String sRetOutput = ssh.exec(sCommandToRun);
        int iReturnCode = ssh.iExitStatus;
        System.out.println("Exit Status Code: "+ssh.iExitStatus);
        ssh.disconnect();
        hSSH.put("ReturnCode",iReturnCode);
        hSSH.put("OutputLog",sRetOutput);
        return hSSH;
    }

    /**
     * This method is used to write value from AAPC property file located in data folder.
     *
     * @param sKey Property key name
     * @param sValue Property key name
     *
     * @return boolean Return true if success otherwise false
     *
     */
    public boolean writePropertyValue(String sKey, String sValue) {

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        String sPropertyFileName=EnvSetup.USER_PROPERTY_FILE_PATH;
        boolean bFlag;
        Properties objConfig = new Properties();
        OutputStream output = null;

        try
        {
            objConfig.load(new FileInputStream(sPropertyFileName)) ;
            objConfig.setProperty(sKey,sValue);
            output = new FileOutputStream(sPropertyFileName);
            objConfig.store(output,"Configuration File Details");

            bFlag = true;
        }
        catch (Exception e) {
            log.error("Exception occurred in {writePropertyValue} method...");
            bFlag = false;
        }
        finally {
            if (output!=null) {
                try {
                    output.close();
                }catch (Exception e) {

                }
            }
        }
        return bFlag;

    }

    /**
     * This method is used to read value from XML file.
     *
     * @param sXMLFileName XML file name
     * @param sXMLNodeExpression XML node query expression. e.g. //School/student/RollNo
     *
     * @return String Return Node value if available else return null value.
     *
     */
    public String getXMLNodeValue (String sXMLFileName, String sXMLNodeExpression) {
        String sNodeValue = null;
        try {

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(sXMLFileName));

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = sXMLNodeExpression;
            Node xmlNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            sNodeValue = xmlNode.getTextContent();


        } catch (Exception e) {
            log.error("Exception occurred in {getXMLNodeDetail} method..." + e.toString());
        }
        return sNodeValue;
    }

    /**
     * This method is used to read value from XML file.
     *
     * @param sXMLFileName XML file name
     * @param sNodeParentName XML node query expression. e.g. School
     * @param sNodeName Node Name e.g. Student
     *
     *
     * @return String Return Node value if available else return null value.
     *
     */
    public String getXMLNodeValue (String sXMLFileName, String sNodeParentName, String sNodeName) {
        String sNodeValue = null;
        try {

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(sXMLFileName));

            doc.getDocumentElement().normalize();


//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            Node nNode = doc.getElementsByTagName(sNodeParentName).item(0);

            if (nNode != null) {
                Element eElement = (Element) nNode;
                sNodeValue = eElement.getElementsByTagName(sNodeName).item(0).getTextContent();
            }
        } catch (Exception e) {
            log.error("Exception occurred in {getXMLNodeDetail} method..." + e.toString());
        }
        return sNodeValue;
    }

    /**
     * This method is used to read value from XML file.
     *
     * @param sXMLFileName XML file name
     * @param sNodeParentName XML node query expression. e.g. School
     * @param sNodeName Node Name e.g. Student
     * @param sAttributeName XML node Attribute Name e.g. @id
     * @param sAttributeValue XML node Attribute value eg. @id=1
     *
     * @return String Return Node value if available else return null value.
     *
     */
    public String getXMLNodeValue (String sXMLFileName, String sNodeParentName, String sNodeName, String sAttributeName, String sAttributeValue) {
        String sNodeValue = null;
        try {
            String sXMLNodeExpression = "//" + sNodeParentName +"/"+sNodeName+"[@"+ sAttributeName +"='"+ sAttributeValue +"']";
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(sXMLFileName));

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = sXMLNodeExpression;
            Node xmlNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);

            sNodeValue = xmlNode.getTextContent();


        } catch (Exception e) {
            log.error("Exception occurred in {getXMLNodeDetail} method..." + e.toString());
        }
        return sNodeValue;
    }

    /**
     * This method is used to read value from property file.
     *
     * @param driver Webdriver to be used for locating element.
     * @param element to be clicked.
     *
     */
    public void contextMenuItemClick(WebDriver driver, WebElement element) {


        Actions action = new Actions(driver);

        action.moveToElement(element);
        this.wait(2);
        action.clickAndHold();
        action.release();
        action.perform();
        this.wait(2);
    }
    /**
     * This method is used to read value from property file.
     *
     * @param sKey Property key name
     * @param sValue Name of the Key Value.
     * @param sPropertyFileName Name of the Property file.
     *
     * @return boolean Return true if success otherwise false
     *
     */
    public boolean writePropertyValue(String sKey, String sValue, String sPropertyFileName) {

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        boolean bFlag;
        Properties objConfig = new Properties();
        OutputStream output = null;
        try
        {

            objConfig.load(new FileInputStream(sPropertyFileName)) ;
            objConfig.setProperty(sKey,sValue);
            output = new FileOutputStream(sPropertyFileName);
            objConfig.store(output, "Configuration File Details");
            bFlag = true;
        }
        catch (Exception e) {
            log.error("Exception occurred in {writePropertyValue} method...");
            bFlag = false;
        }
        finally {
            if (output!=null) {
                try {
                    output.close();
                }catch (Exception e) {

                }
            }
        }
        return bFlag;
    }

    public String getTimeZoneDateValue(String sExistingTimeZone, String sDateTime, String sExpectedTimeZone) {

        String sDateTimeVal = "";
        try {

            String sGMTFirstTimeZone = sExistingTimeZone.split("\\) ")[0].replace("(GMT","").trim();
            String sGMTLastTimeZone = sExpectedTimeZone.split("\\) ")[0].replace("(GMT","").trim();

            String sGMTFirstTimeZoneValue = "";
            int sGMTFirstTimeZoneValueHours=0, sGMTFirstTimeZoneValueMinutes=0;

            String sGMTLastTimeZoneValue = "";
            int sGMTLastTimeZoneValueHours = 0, sGMTLastTimeZoneValueMinutes =0;

            if (sGMTFirstTimeZone.startsWith("+")) {
                sGMTFirstTimeZoneValue = sGMTFirstTimeZone.replace("+","");
                sGMTFirstTimeZoneValueHours = Integer.parseInt(sGMTFirstTimeZoneValue.split(":")[0]);
                sGMTLastTimeZoneValueMinutes = Integer.parseInt(sGMTFirstTimeZoneValue.split(":")[1]);
            }else if (sGMTFirstTimeZone.startsWith("-")){

                sGMTFirstTimeZoneValue = sGMTFirstTimeZone.replace("-","");
                sGMTFirstTimeZoneValueHours = -Integer.parseInt(sGMTFirstTimeZoneValue.split(":")[0]);
                sGMTLastTimeZoneValueMinutes = -Integer.parseInt(sGMTFirstTimeZoneValue.split(":")[1]);
            }


            if (sGMTLastTimeZone.startsWith("+")) {
                sGMTLastTimeZoneValue = sGMTLastTimeZone.replace("+","");
                sGMTLastTimeZoneValueHours = Integer.parseInt(sGMTLastTimeZoneValue.split(":")[0]);
                sGMTLastTimeZoneValueMinutes = Integer.parseInt(sGMTLastTimeZoneValue.split(":")[1]);

            }else if(sGMTLastTimeZone.startsWith("-")) {

                sGMTLastTimeZoneValue = sGMTLastTimeZone.replace("-","");
                sGMTLastTimeZoneValueHours = -Integer.parseInt(sGMTLastTimeZoneValue.split(":")[0]);
                sGMTLastTimeZoneValueMinutes = -Integer.parseInt(sGMTLastTimeZoneValue.split(":")[1]);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            Calendar cal = Calendar.getInstance();

            Date date = dateFormat.parse(sDateTime) ;

            cal.setTime(date);

            cal.add(Calendar.HOUR,- sGMTFirstTimeZoneValueHours);
            cal.add(Calendar.MINUTE,- sGMTFirstTimeZoneValueMinutes);

            cal.add(Calendar.HOUR,sGMTLastTimeZoneValueHours);
            cal.add(Calendar.MINUTE,sGMTLastTimeZoneValueMinutes);
            sDateTimeVal = dateFormat.format(cal.getTime());

        }
        catch (Exception e) {

        }
        return sDateTimeVal;
    }

    /**
     * This method is used to select checkbox
     * @param Chkbox Checkbox Name
     * @param bCheck True/False
     * @return return True if success otherwise false.
     */
    public boolean selectCheckbox(WebElement Chkbox, boolean bCheck) {

        if (bCheck){
            if (!Chkbox.isSelected()){
                Chkbox.click();
            }
        }
        else {
            if (Chkbox.isSelected()){
                Chkbox.click();
            }
        }

        return true;
    }

    /**
     * This method is used to select checkbox using JavaScriptExecutor option
     * @param driver WebDriver Name
     * @param Chkbox Checkbox Name
     * @param bCheck True/False
     * @return return True if success otherwise false.
     */
    public boolean selectCheckbox(WebDriver driver, WebElement Chkbox, boolean bCheck) {

        boolean bAction = false;
        if (bCheck){
            if (!Chkbox.isSelected()){
//Chkbox.click();
                bAction = true;
            }
        }
        else {
            if (Chkbox.isSelected()){
//Chkbox.click();
                bAction = true;
            }
        }

        if (bAction) {
            JavascriptExecutor executor1 = (JavascriptExecutor)driver;
            executor1.executeScript("arguments[0].click();", Chkbox);
        }

        return true;
    }

    /**
     * This method is used to Select/check Checkbox button.
     *
     * @param sHostNameOrIP Linux machine Host Name or IP
     * @param sUserName User name of machine
     * @param sPassword Password of machine
     * @param sSourceFileName Windows File name to copy with full path
     * @param sDestinationPath Linux destination path to copy file.
     *
     * @return boolean Return true if success otherwise false.
     *
     */
    public boolean copyFile(String sHostNameOrIP, String sUserName, String sPassword, String sUtilityPath, String sSourceFileName, String sDestinationPath) {
        try {
            if (sUtilityPath.charAt(sUtilityPath.length()-1) != '\\') {
                sUtilityPath = sUtilityPath + "\\";
            }

            String sCmdToConnect = "cmd.exe /c echo y | \"" + sUtilityPath + "plink.exe" +"\" -ssh " + sHostNameOrIP + " -l " + sUserName + " -pw " + sPassword + " exit";
            String sCmdToRun = "\""+ sUtilityPath +"pscp.exe" + "\" -batch -pw " + sPassword + " -r \"" + sSourceFileName + "\" " + sUserName + "@" + sHostNameOrIP + ":" + sDestinationPath;

            Process process1= Runtime.getRuntime().exec(sCmdToConnect);
            int retVal = process1.waitFor();

            if (retVal != 0) {
                return false;
            }
            Process process2= Runtime.getRuntime().exec(sCmdToRun);
            retVal = process2.waitFor();

            if (retVal != 0) {
                return false;
            }
            return true;

        }catch (Exception e) {
            return false;

        }

    }

    /**
     * This method is used to Retrieves single excel rows for the respective test case Name(column)..
     *
     * @param sExcelFileName Excel file Name
     * @param SQLQuery SQL Query.
     * @return Hashtable with excel column Names and values
     *
     */
    public Hashtable getExcelRow(String sExcelFileName, String SQLQuery){
        Hashtable htRow = new Hashtable();



        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            ResultSet rsExcelRow = readExcelDatabase(sExcelFileName, SQLQuery);

            if (rsExcelRow != null){
                ResultSetMetaData rsmd= rsExcelRow.getMetaData();

                int iColumnCount = rsmd.getColumnCount();

                rsExcelRow.beforeFirst();
                if (rsExcelRow.next())
                {
                    for (int iColNo=0;iColNo < iColumnCount ;iColNo++ )
                    {
                        String sVal = rsExcelRow.getString(iColNo+1);
                        if (sVal == null )
                        {
                            htRow.put(rsmd.getColumnName(iColNo+1),"");
                        }
                        else {
                            htRow.put(rsmd.getColumnName(iColNo+1),sVal);
                        }
                    }
                }
            }
            rsExcelRow.close();

        } catch (Exception e) {
            log.error("Error reading test data from "+sExcelFileName );
        }

        return htRow;
    }

    public String getCustomizedDate(String sDateOrRange, String sDateFormat ) {
//SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        SimpleDateFormat dateFormat = new SimpleDateFormat(sDateFormat);
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if (sDateOrRange.equalsIgnoreCase("today")) {

        } else if (sDateOrRange.equalsIgnoreCase("yesterday")) {
            cal.add(Calendar.DATE, -1);
        } else if (sDateOrRange.equalsIgnoreCase("lastweek")) {
            cal.add(Calendar.DATE, -7);
        } else if (sDateOrRange.equalsIgnoreCase("last2week")) {
            cal.add(Calendar.DATE, -14);
        } else if (sDateOrRange.equalsIgnoreCase("lastmonth")) {
            cal.add(Calendar.MONTH, -1);
        } else if (sDateOrRange.equalsIgnoreCase("lastyear")) {
            cal.add(Calendar.YEAR, -1);
        } else {
            return sDateOrRange;
        }
        return dateFormat.format(cal.getTime());
    }

    /**
     * This method is used to verify specified Web Page loaded completely or not.
     *
     * @param driver Selenium WebDriver object
     *
     * @param secs Specify time in seconds to waitfor
     *
     * @return boolean return true if Page loaded completely otherwise false.
     *
     */
    public boolean waitForPage(WebDriver driver, int secs) {
        try{
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
// Declare an explicit wait
            WebDriverWait wait = new WebDriverWait(driver, secs);

// Wait for page to load completely
            JavascriptExecutor js = (JavascriptExecutor) driver;
// Check the state
            for (int i = 5; i <= secs; ) {
                this.wait(5);
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    break;
                }
                i += 5;
            }
            return true;
        }
        catch (Exception e){
            log.error("Exception occurred in waitForPage method...");
            return false;
        }
    }


    public boolean ElementExists(WebDriver driver,By by)
    {
        boolean returnVal=true;
        if(driver.findElements(by).size()==0)
        {
            returnVal=false;
        }
        return returnVal;
    }

    /**
     * This method is used to Read csv file and retrieves respective data return by SQL query.
     *
     * @param sCSVFileName CSV file Name
     *
     * @return ResultSet JDBC-ODBC recordset object.
     *
     */
    public ResultSet readCSVDatabase(String sCSVFileName) {
        Connection c = null;
        Statement stmnt = null;
        ResultSet rsData = null;
        try {
            log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

            if (this.isFileExists(sCSVFileName) == false){
                log.error("File <" + sCSVFileName + "> does not exists...");
                return rsData;
            }

            File file = new File(sCSVFileName);

            String sFileName = file.getName();
            String sCSVPath = file.getParent();

            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

            c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Text Driver (*.txt; *.csv)};DBQ=" + sCSVPath );
            stmnt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String sSQLQuery = "Select * from " + sFileName ;
            ResultSet rs = stmnt.executeQuery(sSQLQuery);
            rsData = rs;
//rs.close();
//stmnt.close();
//c.close();

        } catch (Exception e) {
            log.error("Error reading CSV database " + sCSVFileName);
        }
        return rsData;
    }

    /***
     * this method is used to get all rows in hashtable array.
     *
     * @param sCSVFileName CSV file Name
     * @return Hashtable[] Return table rows in Hashtable. return null if empty
     */
    public Hashtable[] getCSVRows(String sCSVFileName) {

        Hashtable[] hResSet = null;
        int rowCountInc = 0;
        int colCount,rowCount=0;
        try {

//create a result set
            ResultSet rs = this.readCSVDatabase(sCSVFileName);

//Check for no records
            if(!rs.isBeforeFirst() )
            {
                log.error("No Records Found ");
//Get the column count
                ResultSetMetaData metadata = rs.getMetaData();
                colCount = metadata.getColumnCount();
                log.info("test_table columns : " + colCount);
                hResSet = new Hashtable[rowCount+1];
                Hashtable htTemp = new Hashtable();
                for (int j=1; j<=colCount; j++)
                {
                    String columnName = metadata.getColumnName(j);
                    htTemp.put(j,columnName) ;
//log.info(columnName);
                }

//hResSet[rowCountInc] = htTemp;

            }
            else
            {
//log.info("Query returned record set");
//Get the row count
                if(rs.last())
                {
                    rowCount = rs.getRow();
                    rs.beforeFirst();
                }
//Get the column count
                ResultSetMetaData metadata = rs.getMetaData();
                colCount = metadata.getColumnCount();
//log.info("test_table columns : " + colCount);
                hResSet = new Hashtable[rowCount];
                Hashtable htTemp = new Hashtable();
                Hashtable hColTemp = new Hashtable();
                for (int j=1; j<=colCount; j++)
                {
                    String columnName = metadata.getColumnName(j);
                    hColTemp.put(j,columnName) ;
//log.info(columnName);
                }

//hResSet[rowCountInc] = htTemp;
//rowCountInc++;

                while(rs.next())
                {

                    htTemp = new Hashtable();
                    for (int j=1; j<=colCount; j++)
                    {
                        String sVal = rs.getString(j);

                        if (sVal == null )
                        {
                            htTemp.put(hColTemp.get(j),"");
                        }
                        else
                        {
                            if (sVal.endsWith(".0")) {
                                sVal = sVal.substring(0, sVal.length() - 2);
                            }
                            htTemp.put(hColTemp.get(j),sVal);
                        }
//log.info("Row NO: " + rowCountInc + "Column NO: " + j + "Value " + sVal);
                    }
                    hResSet[rowCountInc] = htTemp;
                    rowCountInc++;
                }


            }
            rs.close();
        } catch (Exception e ) {
            log.error(e.toString());
        }

        return hResSet;
    }

    /**
     * This function is used to export specified sheet data into csv file
     * @param excelFileName Input Excel file name
     * @param sheetName Worksheet name to which data is to be convert
     * @param csvFileName Result or output file name
     *
     * @return boolean Return true if successful else false
     */
    public boolean convertXLS2CSV(String excelFileName, String sheetName, String csvFileName) {
// For storing data into CSV files
        StringBuffer data = new StringBuffer();
        String sVal = "";
        int iHeaderColumnCount = 0;

        try {
            PrintStream stream = new PrintStream(new File(csvFileName));

//FileOutputStream fos = new FileOutputStream(outputFile);

// Get the workbook object for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(excelFileName)));
// Get sheet from the workbook
            HSSFSheet sheet = workbook.getSheet(sheetName);//.getSheetAt(0);
            Cell cell;
            Row row;

// Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                data = new StringBuffer();
                row = rowIterator.next();

                if (iHeaderColumnCount == 0) {
                    iHeaderColumnCount = row.getLastCellNum();
                }


                for (int i = 0; i < iHeaderColumnCount; i++) {
                    cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
                    data.append(cell.getStringCellValue() + ",");
//System.out.print(cell.toString() + ",");
                }

                sVal = data.toString();

                if (sVal.endsWith(",")) {
                    sVal = sVal.substring(0, sVal.length() - 1);
                }
                stream.println(sVal);

            }

            stream.close();
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }

    }

    /**
     * This function is used to get filtered hashtable list based on KeyName
     * @param hSource Hashtable contains Test case data
     * @param sKeyName Column Name
     * @param sValue Column value
     * @return Hashtable[] return list of Hashtable data
     */
    public Hashtable[] getFilterData(Hashtable[] hSource, String sKeyName, String sValue) {

        int iCnt = 0;

        if (hSource!=null) {
            if (hSource[0].containsKey(sKeyName)) {

                for (Hashtable t : hSource) {

                    if (t.get(sKeyName).toString().equals(sValue)) {
                        iCnt = iCnt + 1;
                    }
                }
            }
        }
        if (iCnt ==0) {
            iCnt = 1;
        }
        Hashtable[] hRetData = new Hashtable[iCnt];
        iCnt = 0;
//hRetData[0] = hSource[0];
        for (Hashtable t : hSource) {
            if (t.containsKey(sKeyName)) {
                if (t.get(sKeyName).toString().equals(sValue)) {
                    hRetData[iCnt] = t;
                    iCnt = iCnt +1 ;
                }}
        }


        return hRetData;
    }

    /**
     * This function is used to delete existing file
     * @param sFileName Name of the file
     * @return boolean Return true if successfully deleted otherwise false
     */
    public boolean deleteFile(String sFileName){
        try {
            log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
            if (this.isFileExists(sFileName)) {
                FileUtils.forceDelete(new File(sFileName));

                if (!this.isFileExists(sFileName)) {
                    log.info("File [" + sFileName + "] successfully deleted...");
                }else{
                    log.error("Failed to delete File [" + sFileName + "] ...");
                    return false;
                }
            }else {
                log.info("File [" + sFileName + "] does not exists...");
            }
            return true;
        }
        catch (Exception e){
            new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
            return false;
        }
    }

    /***
     *
     * @param sXMLFileName
     * @param sNodeName
     * @return
     */
    public NodeList ReadXmlFile (String sXMLFileName, String sNodeName) {

        try {

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(sXMLFileName));

            doc.getDocumentElement().normalize();


//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            return doc.getElementsByTagName(sNodeName);


        } catch (Exception e) {
            log.error("Exception occurred in {getXMLNodeDetail} method..." + e.toString());
            return null;
        }

    }

    /**
     * This method is used to read value from property file.
     *
     *
     * @param sPropertyFileName Name of the Property file.
     *
     * @return String Value of the key specified
     *
     */
    public Properties loadPropertyValues(String sPropertyFileName) {

        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Properties objConfigOther = null;
        FileInputStream fs = null;
        try
        {
            fs = new FileInputStream(sPropertyFileName);;
            objConfigOther = new Properties();

            objConfigOther.load(fs);

        }
        catch (Exception e) {
            log.error("Exception occurred in {ReadPropertyValue} method...");

        }
        finally {
            try {
                if (fs!= null) {
                    fs.close();
                }
            }
            catch (Exception e) {

            }
        }
        return objConfigOther;
    }


    public By getLocator(String locator)  {
        //Read value using the logical name as Key

        //Split the value which contains locator type and locator value
        String locatorType = locator.split(":")[0];
        String locatorValue = locator.replace(locatorType+":",""); //locator.split(":")[1];
        //Return a instance of By class based on type of locator
        if(locatorType.toLowerCase().equals("id"))
            return By.id(locatorValue);
        else if(locatorType.toLowerCase().equals("name"))
            return By.name(locatorValue);
        else if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
            return By.className(locatorValue);
        else if((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
            return By.className(locatorValue);
        else if((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
            return By.linkText(locatorValue);
        else if(locatorType.toLowerCase().equals("partiallinktext"))
            return By.partialLinkText(locatorValue);
        else if((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
            return By.cssSelector(locatorValue);
        else if(locatorType.toLowerCase().equals("xpath"))
            return By.xpath(locatorValue);
        else
            log.info("Locator type '" + locatorType + "' not defined!!");
        return null;
    }


    public WebElement getMapObject(String objectName) {

        By by = getLocator(EnvSetup.PROPERTIES_FILE.getProperty(objectName));
        //return EnvSetup.WEBDRIVER.findElement(By.xpath(".//a[text()='Careers']"));
        return  this.eXISTs(EnvSetup.WEBDRIVER,by,30);

    }

    public boolean CallMethod (String methodName, String objectName, String argument )  {

        try {

            Method methodToRun = null;
            String sClassName = "com.lib.UtilityFun";
            Class<?> cClass = Class.forName(sClassName); // convert string classname to class
            Object obj = cClass.newInstance(); // invoke empty constructor


            //Method method = obj.getClass().getMethod(methodName, String.class);

            Method[] methods = obj.getClass().getDeclaredMethods();

            for ( Method method: methods ) {
                if (method.getName().equals(methodName)) {
                    methodToRun = method;
                    break;
                }
            }




            if (methodToRun.getParameterAnnotations().length == 1) {
                Class<?>[] paramTypes = {String.class};
                Method setNameMethod = obj.getClass().getMethod(methodName, paramTypes);
                if (objectName.trim().length() == 0) {
                    setNameMethod.invoke(obj, argument); // pass arg
                }else {
                    setNameMethod.invoke(obj, objectName); // pass arg
                }
            }else if (methodToRun.getParameterAnnotations().length == 2) {
                Class<?>[] paramTypes = {String.class, String.class};
                Method setNameMethod = obj.getClass().getMethod(methodName, paramTypes);
                setNameMethod.invoke(obj, objectName,argument); // pass arg

                }

        }catch (Exception e ) {
            log.info(e.toString());
        }
        return true;
    }


    public boolean LaunchApplicationURL(String sURL) {

        log.info("Launch  URL: " + sURL);

        if (EnvSetup.BROWSER_NAME.equalsIgnoreCase("IE")) {
            JavascriptExecutor js = (JavascriptExecutor) EnvSetup.WEBDRIVER;
            js.executeScript("window.location=arguments[0]; ", sURL);
            this.wait(5);
        } else {
            EnvSetup.WEBDRIVER.get(sURL);
            this.wait(2);
        }

        //Check if IE Certificate Page exists...
        if (sURL.contains("https")) {
            this.setBrowserSSLCertificate(EnvSetup.WEBDRIVER, EnvSetup.BROWSER_NAME);
        }

        return  true;
    }

    public boolean Web_Click(String objectName) {
        try {
            WebElement element = getMapObject(objectName);

            element.click();
        }catch (Exception e) {
            log.info(e.toString());
        }
        return true;
    }

    public boolean Web_SetText(String objectName, String sText) {
        try{
        WebElement element =getMapObject(objectName);

        element.sendKeys(sText+Keys.TAB);
        }catch (Exception e) {
            log.info(e.toString());
        }
        return true;
    }

}







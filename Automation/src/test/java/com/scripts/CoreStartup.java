package com.scripts;

import com.frame.PageHelper;
import com.lib.EnvSetup;
import com.lib.SuiteListener;
import com.lib.UtilityFun;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;


import java.util.Hashtable;
import java.util.Properties;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is contains
 *
 * @version 0.1  -    :
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */
@Listeners({ SuiteListener.class})
public class CoreStartup extends BaseTestClass {

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

    @Test ( description="Launch Firefox browser" )
    public void StartUp () {
        try {
            //utilObj.LaunchApplicationURL("www.synechron.com");
            //Read Object Map property File
            EnvSetup.PROPERTIES_FILE = utilObj.loadPropertyValues(EnvSetup.MASTER_CONFIG_PATH + "objectmap.properties");

            //Read Test case XML file
            NodeList testcaseList = (NodeList) utilObj.ReadXmlFile(EnvSetup.TESTCASE_INPUT_FILE, "TestCase");

            for (int iTcCnt = 0; iTcCnt < testcaseList.getLength(); iTcCnt++) {

                Element testcase = (Element) testcaseList.item(iTcCnt);

                NodeList teststepList = testcase.getElementsByTagName("TestStep");
                for (int iTsCnt = 0; iTsCnt < teststepList.getLength(); iTsCnt++) {

                    Element teststep = (Element) teststepList.item(iTsCnt);

                    String sAction = teststep.getAttribute("Action");
                    String sObjectName = teststep.getAttribute("ObjectName");
                    String sArgument = teststep.getAttribute("Argument");

                    //WebElement element = utilObj.getMapObject (sObjectName);
                    utilObj.CallMethod(sAction, sObjectName, sArgument);
                    //pageHelper.LoginButton.click();

                }

            }
        }catch (Exception e) {
            log.info(e.toString());
        }

    }

    @Test ( description="Launch Firefox browser" , dataProviderClass = UtilityFun.class, dataProvider = "getDataProviderDetails")
    public void DataDrivenTest (Hashtable hData) {

        String sSearchText = hData.get("SearchText").toString();
        System.out.println("My Data driven Test case Is runing for Text [" + sSearchText+ "]");


    }

}

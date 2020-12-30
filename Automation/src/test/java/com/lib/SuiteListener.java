package com.lib;
import org.apache.log4j.Logger;
import org.testng.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
/**
 * This generic listener class is used to take screenshot in case of failure/skip the test case execution.
 *
 * @version 0.1  -  08/18/13   : Integration Team
 *
 * <font size="-2"> <br><br>
 *
 * </br> </br> </font>
 */
public class SuiteListener implements ISuiteListener,ITestListener{
    // Define logger
    protected Logger log = Logger.getLogger(this.getClass().getName());
    private static ThreadLocal<ISuite> ACCESS = new ThreadLocal<ISuite>();
    ITestResult res ;
    UtilityFun objUtil = new UtilityFun();

    public static ISuite getAccess() {
        return ACCESS.get();
    }

    @Override
    public void onStart(ISuite arg0) {
        ACCESS.set(arg0);
    }

    @Override
    public void onFinish(ISuite iSuite) {
        //To change body of implemented methods use File | Settings | File Templates.
        //log.info("***********************SUITE END***************************");
    }

    public void onFinish(ITestContext context) {}

    public void onStart(ITestContext context){}

    /**
     * This Method will be called if Test cases fails.
     * @param result : Instance of ITestResult.
     */
    public void  onTestFailure(ITestResult result){
        Reporter.setCurrentTestResult(result);
        //log.error(result.getThrowable().getStackTrace());
        log.error(result.getThrowable().getMessage());
        objUtil.updateTestExecutionResult(result);
        Reporter.setCurrentTestResult(null);
        log.info("Test Case Execution Status: Failed");
        // Close Browser.
        EnvSetup.closeBrowser();
        log.info("***********************TEST END***************************");
    }

    /**
     * This method will get called if test cases is skipped.
     * @param result :Instance of ITestResult
     */
    @Override
    public void onTestSkipped(ITestResult result){
        Reporter.setCurrentTestResult(result);
        log.error(result.getThrowable().getMessage());
        objUtil.updateTestExecutionResult(result);
        Reporter.setCurrentTestResult(null);
        log.info("Test Case Execution Status: Skipped");
        log.info("***********************TEST END***************************");
    }

    /**
     * This Method will be exeucted by TestNG just before executing actual Test Method.
     * It will read test data into HashTable also sets TESTCASE_START_TIME
     * @param result : Instance of ITestResult
     */
    public void onTestStart(ITestResult result){
        EnvSetup.hTestDetails = objUtil.getTestDetails(result);
        //objUtil.updateTestExecutionResult(result);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        Date date = new Date();
        String sFEATName = "-";
        String jiraID="-";
        if (EnvSetup.hTestDetails != null) {
            if (EnvSetup.hTestDetails.isEmpty() == false && EnvSetup.hTestDetails.containsKey("FEAT") == true){
                if (EnvSetup.hTestDetails.get("FEAT").toString().trim() != "") {
                    sFEATName = EnvSetup.hTestDetails.get("FEAT").toString();
                }
            }
        }
        EnvSetup.TESTCASE_START_TIME = dateFormat.format(date);
        if (result.getParameters().length == 0) {
            EnvSetup.TESTCASE_FEAT = sFEATName;
        }else{
            EnvSetup.TESTCASE_FEAT = ((Hashtable)(result.getParameters()[0])).get("FEAT").toString();
            if (EnvSetup.PRE_NUMBER_FOR_DATA_DRIVEN.equalsIgnoreCase("YES")) {

            }
        }
        if(EnvSetup.hTestDetails.get("JIRA_ID").toString().length()!=0) {
            jiraID = EnvSetup.hTestDetails.get("JIRA_ID").toString();
        }
        EnvSetup.TEST_JIRA_ID=jiraID;
        log.info("");
        log.info("************************TEST START*************************");
        log.info("Executing test case : "+result.getMethod().getXmlTest().getName());
        if (objUtil.checkIfTestToSkip(EnvSetup.hTestDetails)) {
            Reporter.setCurrentTestResult(result);
            Reporter.log("Skipping Testcase : " + result.getMethod().getXmlTest().getName() + " by user.");
            Reporter.setCurrentTestResult(null);
            log.info("Skipping Testcase :  [" + result.getMethod().getXmlTest().getName() + "] by user.");
            throw new SkipException("Skipping Testcase :  " + result.getMethod().getXmlTest().getName() + " by user.");
        }
    }

    /**
     * This method will be executed once test case is successfully completed.
     * This will sets test results in Reporter
     * @param result : Instance of ITestResult
     */
    public void onTestSuccess(ITestResult result){
        Reporter.setCurrentTestResult(result);
        objUtil.updateTestExecutionResult(result);
        Reporter.setCurrentTestResult(null);
        log.info("Test Case Execution Status: Passed");
        log.info("***********************TEST END***************************");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result){}
}

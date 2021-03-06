package com.proj.library;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.frw.Constants.Constants_FRMWRK;
import com.frw.util.WaitUtil;
import com.proj.base.TestBase;
import com.proj.util.CustomExceptions;
import com.proj.util.ErrorUtil;






public class commonMethods extends TestBase{



	static commonMethods cmd;
	public static boolean isDBcon=false;
	public static WebDriver popupdriver=null;
	public static WebDriver popupdriver_bef=null;



	public static commonMethods getInstance(){
		if (cmd == null){
			cmd=new commonMethods();
			logsObj.log("Creating commonFunctions instance...");
		}

		return cmd;
	}

	/**
	 * Navigates to the url specified
	 * @author khshaik
	 * @param URL
	 * @throws Throwable
	 */
	public static void navigateURL(WebDriver driver,String URL) throws Throwable{
		driver.get(URL);
		/*		parentwindow = driver.getWindowHandle();
		ie_Certification(driver);*/
		logsObj.log("launching the AUT "+URL);

	}


	


	/**
	 * Splits the string
	 * @author khshaik
	 * @param stringToSplit
	 * @param delimiter
	 * @return
	 */
	public static String[] splitString(String stringToSplit,String delimiter){
		String[] flag={Constants_FRMWRK.Fail};

		try{
			String[] result = stringToSplit.split(delimiter);
			if (result.length < 2) {						 
				throw new IllegalArgumentException("String not in correct format");					 
			} else {
				flag=result;
			}
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);	
			logsObj.logError("Unable to split the string-"+stringToSplit+" due to erro-", t);
		}


		return flag;

	}


	/**
	 * Checks the test case status
	 * @author khshaik		
	 * @param isTestPass
	 */
	public static void isTestcasePass(boolean isTestPass){
		if(isTestPass==false){
			Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
		}		
	}

	/**
	 * Replace a variable 
	 * @author SAHAMED
	 * @param stringTobeReplaced
	 * @param valueToReplace
	 * @return error details for failure and replaced string for success
	 */
	public static String replaceVariableinString(String stringTobeReplaced,String valueToReplace){
		String flag=stringTobeReplaced;

		try{
			flag=stringTobeReplaced.replaceAll("variable", valueToReplace);
			logsObj.log("The Replaced string is--"+flag);
		}catch(Throwable t){
			flag=Constants_FRMWRK.Error+t;
			logsObj.logError("Cannot Replace string due to error--",t);
		}


		return flag;
	}

	/**
	 * replace a require character in the string
	 * @author SAHAMED
	 * @date Mar 3 2014
	 * @param ToReplaceCharacter
	 * @param stringTobeReplaced
	 * @param valueToReplace
	 * @return error details for failure and replaced string for success
	 */
	public static String replaceString(String ToReplaceCharacter,String stringTobeReplaced,String valueToReplace){
		String flag=stringTobeReplaced;

		try{
			flag=stringTobeReplaced.replaceAll(ToReplaceCharacter, valueToReplace);
			logsObj.log("The Replaced string is--"+flag);
		}catch(Throwable t){
			flag=Constants_FRMWRK.Error+t;
			logsObj.log("Cannot Replace string due to error--"+t);
		}


		return flag;
	}
	/**
	 * Fetches number of keys available in the given Hashtable
	 * @author sahamed
	 * @Date Feb 18 2014
	 * @param hash
	 * @return
	 */
	public static int Hashtable_NumberofKeys(Hashtable<String,String>hash){
		int number=0;
		try{
			if (!hash.isEmpty()){
				@SuppressWarnings("rawtypes")
				Set keys=hash.keySet();
				number=keys.size();
			}
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
		}

		return number;
	}



	/**
	 * Click the certification link on IE Browser
	 * @author sahamed
	 * @Date Jun 2 2014
	 * 
	 */

	public static void ie_Certification(WebDriver driver){
		try{
			if("Certificate Error: Navigation Blocked".equals(driver.getTitle())) {
				driver.navigate().to("javascript:document.getElementById('overridelink').click()");
				logsObj.log("Certificate Error message is displayed and clicked for ie browser");
			}
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
			logsObj.logError("Unable to click the certificate error for ie browser due to error", t);
		}

	}

	public static void getBrowserVersion(WebDriver driver){
		//String name = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		browserVersion = ((RemoteWebDriver) driver).getCapabilities().getVersion();
	}


	public static void switchToDefaultPage(WebDriver driver){
		driver.switchTo().defaultContent();
	}

	/**
	 * @author: sahamed    
	 * @param : identifyBy:By Element is String element
	 * @param : frameName: String which takes the frame name
	 * @return: return a String Pass for Success and Fail for failure
	 * @throws Exception 
	 */
	public static String switchToFrameFromDefault(WebDriver driver,String testcaseName,String locatorType, String frameName) throws Throwable {	
		String flag=Constants_FRMWRK.Pass;
		logsObj.log("Before switchToFrameFromDefault");

		try{
			driver.switchTo().defaultContent();			
		}catch(Exception e){
			isTestPass=Constants_FRMWRK.FalseB;
			logsObj.log ("unable to switch to Default frame");
			flag=Constants_FRMWRK.Fail;
			CustomExceptions.Exit(testcaseName, "switchToFrameFromDefault-Switch to default frame", "Unable to switch to default due to error->"+e+" ,hence cannot execute further test steps..");
		}

		logsObj.log("Sucessfully switched to default frame..Next is "+frameName+" frame..");
		try{
			WebDriverWait wait = new WebDriverWait(driver, 40);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(frameName)));
			//driver.switchTo().frame(ElementMethods.fetchElement(driver, locatorType, frameName));
			logsObj.log("Sucessfully switched to "+frameName+" frame from default..");
		}catch (Exception e){
			isTestPass=Constants_FRMWRK.FalseB;
			logsObj.log ("unable to switch to login frame");
			flag=Constants_FRMWRK.Fail;
			CustomExceptions.Exit(testcaseName, "switchToFrameFromDefault-Switch to "+frameName+" frame from default", "Unable to switch to "+frameName+" frame from defaultdue to error->"+e+" ,hence cannot execute further test steps..");
		}
		return flag;
	}

	public static String switchToSingleFrame(WebDriver driver,String locatorType, String frameName) throws Throwable{		
		String flag=Constants_FRMWRK.Pass;
		try{
			WebDriverWait wait = new WebDriverWait(driver, 40);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath(frameName)));
			//driver.switchTo().frame(ElementMethods.fetchElement(driver, locatorType, frameName));
			logsObj.log("Sucessfully switched to "+frameName+" frame..");
		}catch(Throwable e){
			isTestPass=Constants_FRMWRK.FalseB;
			ErrorUtil.addVerificationFailure(e);
			logsObj.log("Unable to switch to the frame "+frameName+" due to -->"+e.getCause());
			flag=Constants_FRMWRK.Fail;
			CustomExceptions.Exit(testcaseName, "switchToSingleFrame-Switch to "+frameName+" frame ", "Unable to switch to "+frameName+" frame from login due to error->"+e+" ,hence cannot execute further test steps..");
		}

		return flag;
	}

	public static String getCurrentFrameName(WebDriver driver) {
		String str= (String)((JavascriptExecutor) driver).executeScript("return window.frameElement ? window.frameElement.name : 'dsp1';");
		WaitUtil.pause(1);
		return str;
	}

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}

	public static void getViewOfElement(WebDriver driver,WebElement element){
		((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

}










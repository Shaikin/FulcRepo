package com.proj.library;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.frw.Constants.Constants_FRMWRK;
import com.frw.util.FetchWebElement;
import com.frw.util.PageLoadWaitUtil;
import com.frw.util.PopUpUtil;
import com.frw.util.WaitUtil;
import com.proj.Constants.Constants_TimeOuts;
import com.proj.base.TestBase;
import com.proj.objectRepository.ObjRepository;
import com.proj.util.CustomExceptions;
import com.proj.util.Dialogs;
import com.report.reporter.Reporting;



/**
 * 
 * @author shaikka
 *
 */
public class ApplicationMethods_Falcrum extends TestBase{


	/**
	 * Logs into the application with the given user credentials
	 * @param driver
	 * @param userName
	 * @param password
	 * @return
	 * @throws Throwable
	 */
	public static String logIntoApplication(WebDriver driver,String browser,String url,String userName,String password) throws Throwable{
		String flag=Constants_FRMWRK.False;
		if(browser.equalsIgnoreCase("Chrome")||browser.equalsIgnoreCase("firefox") ){
			url="http://"+userName+":"+password+"@"+url;			
			PopUpUtil.checkDefaultPopup(driver, "Login User Authentication popup-Confirmation", "ok");			
			commonMethods.navigateURL(driver,url);
			Reporting.logStep(driver, "Login User credentails", "Successfully entered username and password "+userName+"-"+password, Constants_FRMWRK.Pass);
		}else if (browser.equalsIgnoreCase("ie")){
			url="http://"+url;
			commonMethods.navigateURL(driver,url);
			Dialogs.userAuthentication(browser,url, userName, password);
			//Dialogs.userAuth(CONFIG.getProperty("userDomain"),userName,password);
		}

		PageLoadWaitUtil.waitForPageToLoad(driver);

		/*	Dialogs.userAuthentication(browser, userName, password);*/		

		flag=Constants_FRMWRK.True;
		return flag;
	}
	/**
	 * Launches required browser and logs into the application with given credentails
	 * @param browserType
	 * @param browser
	 * @param url
	 * @param userName
	 * @param password
	 * @return
	 * @throws Throwable
	 */
	public static WebDriver launchBrowserAndlogIntoApplication(String browser,String url,String userName,String password,String refID) throws Throwable{
		WebDriver driver = null;
		browserName=browser;
		driver=Driver.launchBrowser(browser);
		if(browser.equalsIgnoreCase("Chrome")||browser.equalsIgnoreCase("firefox") ){
			url="http://"+userName+":"+password+"@"+url;			
			PopUpUtil.checkDefaultPopup(driver, "Login User Authentication popup-Confirmation", "ok");			
			commonMethods.navigateURL(driver,url);
			Reporting.logStep("Launch and Log into the application", "Launched & logged into the application "+url, Constants_FRMWRK.Pass);
			
		}else if (browser.equalsIgnoreCase("ie")){
			url="http://"+url;
			commonMethods.navigateURL(driver,url);
			Dialogs.userAuthentication(browser,url, CONFIG.getProperty("userDomain")+"\\"+userName, password);
			//Dialogs.userAuth(CONFIG.getProperty("userDomain"),userName,password);
		}

		PageLoadWaitUtil.waitForPageToLoad(driver);
		return driver;
	}
	/**
	 * Logs out of the application
	 * @author shaikka	
	 * @param driver
	 * @return
	 * @throws Throwable
	 */

	public static String logOutFromApplication(WebDriver driver) throws Throwable{

		String flag=Constants_FRMWRK.True;		
		WebElement element;
		//closeAllDialogs(driver, refID, testcaseName);
		commonMethods.switchToDefaultPage(driver);
		element=FetchWebElement.waitForElement(driver, Constants_FRMWRK.FindElementByID, ObjRepository.link_user, Constants_TimeOuts.Element_TimeOut);

		if(element!=null){
			try{
				WaitUtil.pause(1);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
				Thread.sleep(500); 
				element.click();
				Reporting.logStep(driver, refID, "Logout- Click on Open Menu in the Home Page", "Open Menu exists and Clicked", Constants_FRMWRK.Pass);
			}catch(Throwable t){
				isTestPass=false;
				flag=Constants_FRMWRK.False;
				Reporting.logStep(driver, refID, "Logout-Click on Open Menu in the Home Page", "Open Menu exists and but not Clicked"+" due to error-"+t, Constants_FRMWRK.Fail);
			}

		}else{
			isTestPass=false;
			flag=Constants_FRMWRK.False;
			Reporting.logStep(driver, refID, "Logout- Click on Open Menu in the Home Page", "Open Menu doesnot exists ", Constants_FRMWRK.Fail);
		}


		element=FetchWebElement.waitForElement(driver, Constants_FRMWRK.FindElementByXPATH, ObjRepository.link_signOut, Constants_TimeOuts.Element_TimeOut);

		if(element!=null){
			try{
				WaitUtil.pause(1);
				element.click();
				Reporting.logStep(driver, refID, "Logout- Click on Sign Out", "Sign Out exists and Clicked", Constants_FRMWRK.Pass);
			}catch(Throwable t){
				isTestPass=false;
				flag=Constants_FRMWRK.False;
				Reporting.logStep(driver, refID, "Logout- Click on Sign Out", "Sign Out exists and but not Clicked"+" due to error-"+t, Constants_FRMWRK.Fail);
			}

		}else{
			isTestPass=false;
			flag=Constants_FRMWRK.False;
			Reporting.logStep(driver, refID, "Logout- Click on Sign Out", "Sign Out doesnot exists ", Constants_FRMWRK.Fail);
		}

		//PageLoadWaitUtil.waitForPageToLoad(driver);
		return flag;
	}


	public static String logOutFromApplicationAndcloseBrowser(WebDriver driver) throws Throwable{
		WaitUtil.pause(2);
		String flag=Constants_FRMWRK.True;				
		flag=logOutFromApplication(driver);
		PopUpUtil.checkDefaultPopup(driver, "ok");
		Driver.close(driver);
		return flag;
	}

	public static void closeAllDialogs(WebDriver driver,String refID,String testcaseName){
		int counter=1;
		commonMethods.switchToDefaultPage(driver);
		List <WebElement> elements=ElementMethods.fetchElements(driver, Constants_FRMWRK.FindElementByXPATH, "//*[@title='Close dialog']");
		WaitUtil.pause(Constants_TimeOuts.generic_TimeOut);		
		if(elements.size()!=0 && counter <10){
			System.out.println("Number of Close icons are "+elements.size());
			for (WebElement element :elements){
				WaitUtil.pause(Constants_TimeOuts.generic_TimeOut);	
				if(element!=null && element.isDisplayed()==true){
					commonMethods.getViewOfElement(driver, element);
					element.click();
				}
				//WaitUtil.pause(3);		
				WaitUtil.pause(Constants_TimeOuts.generic_TimeOut);	
			}
			elements=ElementMethods.fetchElements(driver, Constants_FRMWRK.FindElementByXPATH, "//*[@title='Close dialog']");
			//WaitUtil.pause(3);	
			WaitUtil.pause(Constants_TimeOuts.generic_TimeOut);	
			if(elements.size()!=0 && counter <10){
				counter=counter+1;
				System.out.println("Number of Close icons are still "+elements.size());
				closeAllDialogs(driver, refID, testcaseName);
			}else if (elements.size()==0 && counter <10){
				Reporting.logStep(driver, "Closing the popup dialogs", "All Opened dialogs are closed", Constants_FRMWRK.Pass);
			}else if (elements.size()!=0 && counter >=10){
				isTestPass=Constants_FRMWRK.FalseB;
				Reporting.logStep(driver, "Closing the popup dialogs", "Unable to close all opened dialogs after 10 attempts", Constants_FRMWRK.Fail);
			}
		}		
	}
	
	public static int getApplicationFrameCount(WebDriver driver){
		int flag=0;
		commonMethods.switchToDefaultPage(driver);
		List<WebElement> frames=FetchWebElement.visibleElementsList(driver, Constants_FRMWRK.FindElementByXPATH, ObjRepository.frame_single);
		if(frames.size()!=0){
			flag=frames.size();
		}
		return flag;
	}
	public static void switchToLatestDLGframe(WebDriver driver,String testcasename) throws Throwable{
		String frameName;
		WaitUtil.pause(2);
		try{
			int frames=getApplicationFrameCount(driver);
			if(frames!=0){
				frameName=ObjRepository.frame_list_pattern;
				frameName=frameName.replaceAll("framelist", String.valueOf(frames));
				commonMethods.switchToFrameFromDefault(driver, testcasename, Constants_FRMWRK.FindElementByXPATH, frameName);
			}
		}catch (Throwable t ){
			CustomExceptions.Exit(testcasename, "Switch to latest frame -Failure", "Unable to switch the latest frame expected due to error-"+commonMethods.getStackTrace(t));
		}
		
		
	}
}

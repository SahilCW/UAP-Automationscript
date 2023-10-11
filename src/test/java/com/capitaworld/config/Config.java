package com.capitaworld.config;


import com.capitaworld.commonutil.Constants;
import com.capitaworld.commonutil.DatabaseConnection;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Config {

    public static WebDriver driver=null ;
    /* Minimum requirement for test configuration */
    public static String baseUrl;
    public static String environment; // Test url
    protected String seleniumHub; // Selenium hub IP
    protected String seleniumHubPort; // Selenium hub port
    protected String targetBrowser; // Target browser
    protected static String test_data_folder_path = null;
    protected static String screenshot_folder_path = null;
    public static String currentTest;
    DatabaseConnection dbConnection=null;
    protected static Connection connection;

    private String hostaddress;

    Enumeration e;

    File file = new File("Resources/chromedriver");
    public String path = file.getAbsolutePath();

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeSuite(alwaysRun = true)
    public void serverConfig() {
        //Create Connection of Database
        connection = null;
        System.out.println("Before suite method");
    }

    /**
     * Fetch Configuration From suite like url and seleniumPort etc.
     *
     * @param testContext
     * @throws IOException
     */
    @BeforeTest(alwaysRun = true)
    public void fetchSuiteConfiguration(ITestContext testContext)
            throws IOException {

        environment = testContext.getCurrentXmlTest().getParameter("env");
        System.out.println(environment);
        seleniumHub = testContext.getCurrentXmlTest().getParameter(
                "selenium.host");
        System.out.println(seleniumHub);
        seleniumHubPort = testContext.getCurrentXmlTest().getParameter(
                "selenium.port");
        System.out.println(seleniumHubPort);
        targetBrowser = testContext.getCurrentXmlTest().getParameter("browser");
        System.out.println(targetBrowser);
        connection= DatabaseConnection.getConnection(connection,environment);
    }

    /**
     * Make Browser Launch from This method.
     *
     * @param method
     * @throws MalformedURLException
     * @throws FileNotFoundException
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) throws MalformedURLException, FileNotFoundException {

        currentTest = method.getName(); // get Name of current test.

        URL remote_grid = new URL("http://" + seleniumHub + ":" + seleniumHubPort + "/wd/hub");

        String SCREENSHOT_FOLDER_NAME = "screenshots";
        String TESTDATA_FOLDER_NAME = "test_data";

        test_data_folder_path = new File(TESTDATA_FOLDER_NAME)
                .getAbsolutePath();
        screenshot_folder_path = new File(SCREENSHOT_FOLDER_NAME)
                .getAbsolutePath();

        DesiredCapabilities capability = null;
        // For Firefox.
        if (targetBrowser == null || targetBrowser.contains("firefox")) {

            FirefoxProfile profile = new FirefoxProfile();
            // Extra preference for Firefox
            profile.setPreference("dom.max_chrome_script_run_time", "999");
            profile.setPreference("dom.max_script_run_time", "999");
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download.useDownloadDir", true);
            profile.setPreference("browser.download.manager.showWhenStarting",false);
            //profile.setEnableNativeEvents(true);
            //profile.setPreference("network.http.use-cache", false);
            capability = DesiredCapabilities.firefox();
            capability.setJavascriptEnabled(true);
            capability.setCapability(FirefoxDriver.PROFILE, profile);
            System.out.println("==firefox Driver==");
        }

        // For Chrome.
        else if (targetBrowser.contains("chrome"))
        {
            System.setProperty("webdriver.chrome.driver","D:\\chromedriver.exe");
            System.setProperty("webdriver.chrome.verboseLogging", "true");
            System.setProperty("webdriver.chrome.logfile", "D:\\chromedriver.log");


			capability = DesiredCapabilities.chrome();


            ChromeOptions chromeOptions = new ChromeOptions();
            // chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--disable-gpu");
//			chromeOptions.addArguments("--log-level=3");
//			chromeOptions.addArguments("--user-agent=New User Agent");
//			chromeOptions.addArguments("user-data-dir=C:\\Users\\sahil.patel\\AppData\\Local\\Google\\Chrome\\User Data");
//			chromeOptions.addArguments("--profile-directory=Profile 1");
            //chromeOptions.addArguments("chrome.switches" ,"--disable-extensions");
            chromeOptions.addArguments("--start-maximized");
            chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
            // chromeOptions.addArguments("window-size=1800x1000");
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            //prefs.put("download.default_directory", "E:\\Tushar\\WorkSpace\\cw-automation\\Resources");
            chromeOptions.setExperimentalOption("prefs", prefs);

            capability.setCapability(ChromeOptions.CAPABILITY,chromeOptions);

}
        // change Remote Grid
        System.out.println(remote_grid);
        System.out.println("set chrome browser & go to select URL");
        driver = new RemoteWebDriver(remote_grid,capability);
        if (environment.equalsIgnoreCase(Constants.QAENVIRONMENT)) {
            //driver.get(qaEnvironment);
            baseUrl = Constants.QA_URL;
        } else {
            System.out.print("Environment setup null");
        }
        driver.get(baseUrl);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    /**
     * Take Screenshot after Test Fail and attach it to Report.
     *
     * @param testResult
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult testResult) {
        try {
            String testName = testResult.getName();

            if (!testResult.isSuccess()) {
                /* Print test result to Console */
                System.out.println("TEST FAILED - " + testName);
                System.out.println("ERROR MESSAGE: "
                        + testResult.getThrowable());
                Reporter.setCurrentTestResult(testResult);
                //testResult.getMethod().getMethodName();
                /* Make a screenshot for test that failed */
                String screenshotName = testName + getCurrentTimeStampString();

                System.out.println(getCurrentTimeStampString());
                Reporter.log("<br> <b>Please look to the screenshot -</b>");
                makeScreenshot(driver, screenshotName);
            }
            else {

                System.out.println("TEST PASSED - " + testName + "\n");
            }

			/*Reporter.log("<br/> Logout Successfully Performed.");
			driver.findElement(By.xpath("html/body/div[1]/nav/div/div[3]/div[2]/a[2]")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//a[text()='Logout']")).click();*/

            driver.manage().deleteAllCookies();
//            driver.quit();




        } catch (Throwable throwable) {

        }
    }

    @AfterSuite
    public void endServer() throws IOException, InterruptedException, SQLException
    {
        // Stop the Server and Node Process.
        //startAndStopServerAndNode("pkill -f 'java -jar /home/discusit/Desktop/selenium-server-standalone-2.48.2.jar'");

        //Closing Connection of Database
//        connection.close();
    }

    public  String getCurrentTimeStampString() {

        Date myDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String myDateString = sdf.format(myDate);
        return myDateString;
    }

    public static String getCurrentdateString() {

        Date myDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
        String myDateString = sdf.format(myDate);
        return myDateString;
    }

    public void makeScreenshot(WebDriver driver, String screenshotName) {

        File ssFile=null;
        WebDriver augmentedDriver = new Augmenter().augment(driver);

        /* Take a screenshot */
        File screenshot = ((TakesScreenshot) augmentedDriver)
                .getScreenshotAs(OutputType.FILE);
        String nameWithExtention = screenshotName + ".png";

        /* Copy screenshot to specific folder */
        try {

            String reportFolder = "test-output" + File.separator;
            String screenshotsFolder = "screenshots";
            File screenshotFolder = new File(reportFolder + screenshotsFolder);
            if (!screenshotFolder.getAbsoluteFile().exists()) {
                screenshotFolder.mkdir();
            }
            ssFile=new File(reportFolder+ File.separator + screenshotsFolder + File.separator+ nameWithExtention);
            FileUtils.copyFile(screenshot,ssFile.getAbsoluteFile());
        } catch (IOException e) {
            Reporter.log("Failed to capture screenshot: " + e.getMessage());
        }
        // Add ScreenShot Link to Report
        Reporter.log(getScreenshotLink(ssFile.getAbsolutePath(), nameWithExtention));
    }

    /**
     * Method for Getting Link To TestNG Report of ScreenShot.
     *
     * @param screenshot_name
     * @param link_text
     * @return
     */
    public String getScreenshotLink(String screenshot_name, String link_text)
    {
        //for IP Address

        try
        {
            e = NetworkInterface.getNetworkInterfaces();
        }
        catch (SocketException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                if(i.isSiteLocalAddress())
                {
                    hostaddress=i.getHostAddress();
                    System.out.println(hostaddress);
                }
            }
        }

        Reporter.log("<br><Strong><font color=#FF0000>--Failed</font></strong>");
        return "<a href='"+ screenshot_name	+ "' target='_new'>" + link_text + "</a>";

        /**
         * For Local
         * ../test-output ----http://192.168.1.121/qms/screenshots_Jenkins_Trunk/
         */
    }

    /**
     * to Print Message in console
     *
     * @param steplog
     * @param message
     * @return
     */
    public static int log(int steplog, String message) {
        Reporter.log("<br />Step " + steplog + ":-" + message);
        steplog++;
        return steplog;
    }

}

package com.capitaworld.commonutil;


import com.capitaworld.config.Config;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CommonMethods extends Config {

    public static List<String> getPropertyValue(String testUrl)	throws IOException
    {
        List<String> value = new ArrayList<String>();
        try {

            if(testUrl.equalsIgnoreCase("qa")) {
                value.add("jdbc:postgresql://10.48.163.198:5432/sugam_udyam_assist_portal?autoReconnect=true&useSSL=false");
                value.add("postgres");
                value.add("9g&8gwvsQT84JP");

            }else if(testUrl.equalsIgnoreCase("uat")) {

                value.add("jdbc:oracle:thin:@oracle-db-qa.c1tey9r6gnam.ap-south-1.rds.amazonaws.com:1521?autoReconnect=true&useSSL=false");
                value.add("admin");
                value.add("DyyyK]h9");
            }else {
                System.out.println("property value null");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        return value;
    }

    /**
     * Wait Method
     *
     * @param seconds
     * @throws InterruptedException
     */
    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }


    public static void refresh(WebDriver driver) throws InterruptedException{

        driver.navigate().refresh();
        sleep(5);
    }
    /**
     * Perform Drag and Drop
     *
     * @param from
     * @param to
     */
    public void DragAndDrop(WebElement from, WebElement to) {
        Actions action = new Actions(driver);
        org.openqa.selenium.interactions.Action dragandDrop = action.clickAndHold(from).moveToElement(to).release(to)
                .build();
        dragandDrop.perform();
    }

    /**
     * perform hover Action on Elements
     *
     * @param ele
     * @param linkText
     */
    public void hover(WebElement ele, String linkText) {
        Actions action = new Actions(driver);
        action.moveToElement(ele).build().perform();
        driver.findElement(By.linkText(linkText)).click();

    }
    /**
     * Get selected value from ComboBox
     *
     * @param element
     * @return
     */
    public static String getSelectedValue(WebElement element) {
        String str = new Select(element).getFirstSelectedOption().getText().trim();
        return str;
    }

    /**
     * Method for get Integer(value) value from ComboBox
     *
     * @param ele
     * @return
     */
    public static int getSelectedValueInInteger(WebElement ele) {

        int value=Integer.parseInt(new Select(ele).getFirstSelectedOption().getAttribute("value").trim().split(":")[1]);
        return value;
    }

    /**
     * Method for get String(label) value from ComboBox
     *
     * @param ele
     * @return
     */
    public static String getSelectedValueInString(WebElement ele) {

        String value=new Select(ele).getFirstSelectedOption().getAttribute("label").trim();
        return value;
    }
    public static boolean isElementPresent(By by, WebDriver driver) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check element present in DOM
     * @param by
     * @param driver
     * @return
     */
    public static boolean isElementPresentClick(By by, WebDriver driver) {
        try {
            driver.findElement(by).click()	;

            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /*
    * get DB value in Boolean
    */
    public static boolean getValueDB(String query,String columnName, Connection connection) {

        boolean str= false;
        try {
            Statement stmt = connection.createStatement();
            // example query ==> "SELECT * FROM users.users;"
            //System.out.println("Query is--"+query);
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next())
            {
                if(columnName.isEmpty())
                {
                    str= rs.getBoolean(1);
                }
                else
                {
                    str= rs.getBoolean(columnName);
                }

            }	//System.out.println("Found value is--"+value);
        } catch (Exception e) {
            System.out.println(e);
        }
        return str;
    }
    /*
     * get DB value in String
     */
    public static Object getValuefromDB(String query, String columnName, Connection connection)
    {
        String value= "";
        try {
            Statement stmt = connection.createStatement();
            // example query ==> "SELECT * FROM users.users;"
            //System.out.println("Query is--"+query);
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next())
            {
                if(columnName.isEmpty())
                {
                    value= rs.getString(1);
                }
                else
                {
                    value= rs.getString(columnName);
                }

                //System.out.println("Found value is--"+value);
            }
            else
            {
                System.out.println("Result set found null");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return value;
    }
    /*
     * get DB list value in Sting list
     */
    public static List<String> getListOfValuefromDB(String query, String columnName, Connection connection)
    {

        List<String> value= new ArrayList<String>();
        try {
            Statement stmt = connection.createStatement();
            // example query ==> "SELECT * FROM users.users;"
            //System.out.println("Query is--"+query);
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next())
            {
                if(columnName.isEmpty())
                {
                    value.add(rs.getString(1));
                }
                else
                {
                    value.add(rs.getString(columnName));
                }
                //System.out.println("Found value is--"+value);

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return value;
    }


    /**
     * Method to click on element using Java script
     * @param ele
     * @param driver
     */
    public static void clickOnElementUsingJS(WebElement ele, WebDriver driver)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", ele);
    }

    /**
     * Method to click on element using action
     * @param ele
     * @param driver
     */
    public static void moveToElementAndClick(WebElement ele, WebDriver driver) {
        Actions actions= new Actions(driver);
        actions.moveToElement(ele).click().build().perform();
    }

    /**
     * Method to view on element using Java script
     * @param ele
     * @param driver
     */
    public static void viewOnElementUsingJS(WebElement ele, WebDriver driver)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", ele);
    }

    /**
     * Method to Get text of element using Java script
     * @param ele
     * @param driver
     */
    public static String getTextOfElementUsingJS(WebElement ele, WebDriver driver)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", ele);
        return ele.getText().trim();
    }

    /**
     * Method to scroll browser window using Java script
     * @param ele
     * @param driver
     */
    public static void scrollToViewElementUsingJS(WebElement ele, WebDriver driver)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,"+ele.getLocation().x+")");
    }
    /*
     *
     * @param sheetName
     * @param rownum -- Value should be counted Starting from 0
     * @param column -- Column should be Passed in A to Z
     * @return
     */
    public static String valueFromExcel(int rownum, String column, XSSFWorkbook file)
    {
        DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        String  value="";
        try {
            //FileInputStream file = new FileInputStream(scoring_File);

            // Create Workbook instance holding reference to .xlsx file
            //

            // Get first/desired sheet from the workbook
            XSSFSheet sheet = file.getSheetAt(0);

            value=df.formatCellValue(sheet.getRow(rownum).getCell(CellReference.convertColStringToIndex(column)));
            //System.out.println("Found Value on row number-"+rownum+", and Cloumne Number-"+column+", Value is--"+value);
            //System.out.println("Value for Row number--"+rownum+", Value is--"+value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return value.trim();
    }


    /*
     *
     * @param rownum
     * @param column
     * @param file
     * @return
     */
    public static int returnRowCount(String parameterName,XSSFWorkbook file)
    {
        DataFormatter df= new DataFormatter();

        int rownum = 0;
        try {

            XSSFSheet sheet = file.getSheetAt(0);
            System.out.println("Last Line of File--"+sheet.getLastRowNum());
            for(int i=1; i <sheet.getLastRowNum();i++)
            {
                String str=df.formatCellValue(sheet.getRow(i).getCell(CellReference.convertColStringToIndex("A"))).trim();
                if(str.equals(parameterName))
                {
                    rownum=i;
                    break;
                }
            }

        }
        catch (Exception e) {
            System.out.println("Found Error for row count--"+rownum);
            e.printStackTrace();
        }
        System.out.println("Returning Row Numbr for parameter--"+parameterName+ ", And Row Number is--"+rownum);
        return rownum;
    }

    /**
     * Return row count from sheet
     * @param parameterName
     * @param file
     * @return
     */
    public static int returnRowCount(String parameterName,XSSFSheet file)
    {
        DataFormatter df= new DataFormatter();

        int rownum = 0;
        try {

            //XSSFSheet sheet = file.getSheetAt(0);
            //  System.out.println("Last Line of File--"+sheet.getLastRowNum());
            for(int i=3; i <file.getLastRowNum();i++)
            {
                String str=df.formatCellValue(file.getRow(i).getCell(CellReference.convertColStringToIndex("A")));

                if(str.equals(parameterName))
                {
                    rownum=i;
                    break;
                }
            }

        }
        catch (Exception e) {
            System.out.println("Found Error for row count--"+rownum);
            e.printStackTrace();
        }
        System.out.println("Returning Row Numbr for parameter--"+parameterName+ ", And Row Number is--"+rownum);
        return rownum;
    }


    public static boolean booleanValueFromExcel(int rownum,String column,XSSFWorkbook file)
    {
        DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        boolean  value=false;
        try {
            XSSFSheet sheet = file.getSheetAt(0);
            String v=df.formatCellValue(sheet.getRow(rownum).getCell(CellReference.convertColStringToIndex(column)));
            value = v.equalsIgnoreCase("TRUE") ? true : false;
        }
        catch (Exception e) {
            System.out.println("Found Error for row count--"+rownum);
            e.printStackTrace();
        }
        return value;
    }
    /**
     * Method for write String value to Excel
     *
     * @param rownum -- Value should be counted Starting from 0
     * @param column -- Column should be Passed in A to Z
     * @param file
     * @param value
     */

    public static void valueToExcel(int rownum, String column, Sheet file, String value)
    {
        //DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        try {
            file.getRow(rownum).getCell(CellReference.convertColStringToIndex(column)).setCellValue(value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Value = " + value);
    }

    public static void valueToExcel(int rownum,int column,Sheet file, String value)
    {
        //DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        try {
            file.getRow(rownum).getCell(column).setCellValue(value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Value = " + value);
    }

    /**
     * Method for write int value to Excel
     * @param rownum
     * @param column -- Column should be Passed in 0 to up-to number of cell in row
     * @param file
     * @param value
     */
    public static void valueToExcel(int rownum, int column, Sheet file, int value)
    {
        //DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        try {

            file.getRow(rownum).getCell(column).setCellValue(value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Value = " + value);
    }

    /**
     * Method for write Double value to Excel
     * @param rownum
     * @param column -- Column should be Passed in A to Z
     * @param file
     * @param value
     */
    public static void valueToExcel(int rownum, int column, Sheet file, double value)
    {
        //DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        try {

            file.getRow(rownum).getCell(column).setCellValue(value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Value = " + value);
    }
    /*
     * Method for get boolean value from excel
     * @param sheetName
     * @param rownum -- Value should be counted Starting from 0
     * @param column -- Column should be Passed in A to Z
     * @return
     */
    public static boolean booleanValueFromExcel(int rownum,String column,XSSFSheet file)
    {
        DataFormatter df= new DataFormatter();

        //File scoring_File=new File("Resources/scoringModel.xlsx");
        boolean  value= false;
        try {

            String v=df.formatCellValue(file.getRow(rownum).getCell(CellReference.convertColStringToIndex(column)));
            value = v.equalsIgnoreCase("TRUE") ? true : false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Value = " + value);
        return value;
    }
    /**
     * Comparing Value and Print in report if not matched
     * @param value1 - Should be original Value
     * @param value2 - Should be value that can be matched
     * @param isequal - True for equal check and false for contains
     */
    public static void compareandPrint(String value1, String value2, String msg,boolean isequal)
    {
        if(isequal)
        {
            if(value1.equalsIgnoreCase(value2))
            {
                System.out.println("matched");
            }
            else
            {
                Reporter.log("<br/>"+msg+" is not matched");
                Reporter.log("original Value is==>"+value1 + ", Found value is==>"+value2);
            }

        }
        else
        {
            if(value1.contains(value2))
            {
                System.out.println("matched");
            }
            else
            {
                Reporter.log("<br/>"+msg+" is not matched");
                Reporter.log("original Value is==>"+value1 +", Found value is==>"+value2);
            }

        }

    }

    /**
     * Comparing Value and Print in report if not matched
     * @param value1 - original int Value
     * @param value2 - Should be compare
     * @param msg
     */
    public static void compareandPrint(int value1, int value2, String msg)
    {
        if(value1==value2)
        {
            System.out.println("matched");
        }
        else
        {
            Reporter.log("<br/>"+msg+" is not matched");
            Reporter.log("original Value is--"+value1 +", Found value is--"+value2);
        }

    }
    /**
     * Method to Element is loaded and Send keys
     * @param ele
     * @param driver
     * @param key
     * @throws Error
     */
    @SuppressWarnings("deprecation")
    public static void isLoadedAndSendkeys(final WebElement ele, WebDriver driver, String key) throws Error {
        new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(new Function<WebDriver, Boolean>() {

                    @Override
                    public Boolean apply(WebDriver webDriver) {
                        final WebElement element = ele;
                        return element != null && element.isDisplayed();
                    }
                });
        ele.clear();
        ele.sendKeys(key);
    }

    /**
     * Return Element and click
     * @param ele
     * @param driver
     * @throws Error
     */
    @SuppressWarnings("deprecation")
    public static void isLoadedAndClick(final WebElement ele, WebDriver driver) throws Error {
        new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
                .ignoring(Exception.class).until(new Function<WebDriver, Boolean>() {

            @Override
            public Boolean apply(WebDriver webDriver) {
                final WebElement element = ele;
                return element != null && element.isDisplayed();
            }
        });
        ele.click();
    }

    public static String returnStringforMail(int pass, int fail, int total, int skip) {
        String str = " <HTML> <BODY> <div style=\"width:100%;text-align:center;background-color:#438EB9;font-weight:bold;color:#fff;font-size:15;\"> <label>Automation Test Report</label>  </div>  <br/> Hi Team,<br/> <br/> <br/> PFA for Automation Test Report. Download it and Open it in Browser. <br/> <br/> <table style=\"border-spacing: 0;border-collapse: collapse;\"> <tr>  <th style=\"border: 1px solid black !important;padding:2px;text-align:left;\">Passed</th>"
                + "<td style=\"border: 1px solid black !important;padding:2px;\">" + pass
                + "</td> <th style=\"border: 1px solid black !important;padding:2px;text-align:left;\">Failed</th> <td style=\"border: 1px solid black !important;padding:2px;\">"
                + fail
                + "</td><th style=\"border: 1px solid black !important;padding:2px;text-align:left;\">Skipped</th><td style=\"border: 1px solid black !important;padding:2px;\">"
                + skip
                + "</td></tr><tr><tr><th style=\"border: 1px solid black !important;padding:2px;text-align:left;\">Total </th> <td style=\"border: 1px solid black !important;padding:2px;\" colspan=\"5\">"
                + total + "</td></tr><tr></table>" + "<br/><br/> Warm Regards, <br/> OPL QA Team<br/><br/>"
                + "<small>This eMail is automatically triggered by the <b>Online PSB Loans - Automation Scripts</b>. If you feel it has been received in error, please contact your IT team.</small>"
                + "</BODY></HTML>";

        return str;

    }

    public static String getCurrentdateString() {

        Date myDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
        String myDateString = sdf.format(myDate);
        return myDateString;
    }

    private static final String ALGORITHM = "AES";
    private static final String KEY = "C@p!ta@W0rld#U$d";
    public static String decriptWithKey(String encryptedText) {
        // do some decryption
        try {
            byte[] keyBytes = Arrays.copyOf(KEY.getBytes("ASCII"), 16);

            SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            // byte[] ciphertextBytes = cipher.doFinal(cleartext);

            return new String(cipher.doFinal(Hex.decodeHex(encryptedText.toCharArray())));
        } catch (Exception e) {
        }
        return null;
    }

    public static String encryptionWithKey(String plainText) {
        // do some encryption
        try {
            byte[] keyBytes = Arrays.copyOf(KEY.getBytes("ASCII"), 16);

            SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cleartext = plainText.getBytes("UTF-8");
            byte[] ciphertextBytes = cipher.doFinal(cleartext);

            return new String(Hex.encodeHex(ciphertextBytes));
        } catch (Exception e) {
        }
        return null;
    }

    public static List<String> usingSubstringMethod(String text, int n) {
        List<String> results = new ArrayList<>();
        int length = text.length();

        for (int i = 0; i < length; i += n) {
            results.add(text.substring(i, Math.min(length, i + n)));
        }

        return results;
    }


}
package com.capitaworld.indexPages;
import com.capitaworld.Pages.MFIFlowPages;
import com.capitaworld.config.*;
import com.capitaworld.proxy.logInProxy;
import com.capitaworld.commonutil.CommonMethods;
import com.capitaworld.commonutil.Constants;
import com.capitaworld.commonutil.DataUtil;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.poi.hssf.record.PageBreakRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;


import java.awt.*;
import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class flowCommonMethods extends AbstractPage {
    Map<String, Object> details;
    DataUtil util = null;

    public flowCommonMethods(WebDriver driver) {
        super(driver);
        System.out.println("Driver instance created---" + driver.getTitle());
        util = DataUtil.getInstance();
        details = util.getDetails();
        // TODO Auto-generated constructor stub
    }

//    public static String filePath = "D:\\Automatio Script\\UAP-Automationscript\\Resources\\Scenario 16 MFI flow (1).xlsb.enc";
    //login user
    @FindBy(xpath = "//span[contains(text(),'Login')]")
    private WebElement homeLoginBtn;
    @FindBy(xpath = "//input[@formcontrolname='email']")
    private WebElement email;
    @FindBy(xpath = "//input[@formcontrolname='password']")
    private WebElement password;
    @FindBy(xpath = "//span[contains(text(),'Login')]")
    private WebElement loginBtn;

    //Upload File
    @FindBy(xpath = "//div[1]/div[1]/h2[1]")
    private WebElement pageHeading;
    @FindBy(xpath = "//span[contains(text(),'Bulk Upload')]")
    private WebElement bulkUploadBtn;
    @FindBy(xpath = "//span[contains(text(),'Records Status')]")
    private WebElement recordStatusBtn;
    @FindBy(xpath = "//span[contains(text(),'URN Status')]")
    private WebElement urnStatusBtn;
    @FindBy(xpath = "//input[@id='fileUpload']")
    private WebElement chooseFileBtn;
    @FindBy(xpath = "//span[contains(text(),'Upload File')]")
    private WebElement uploadFileBtn;
    @FindBy(xpath = "//tbody/tr[1]/td[8]")
    private WebElement recordsUploaded;
    @FindBy(xpath = "//tbody/tr[1]/td[7]")
    private WebElement recordsReturned;
    @FindBy(xpath = "//tbody/tr[1]/td[6]")
    private WebElement recordsAccepted;
    @FindBy(xpath = "//tbody/tr[1]/td[4]")
    private WebElement fileName;
    @FindBy(xpath = "//tbody/tr[1]/td[9]/button/span/mat-icon")
    private WebElement viewUploadRec;
    @FindBy(xpath = "//span[contains(text(),'Records Accepted for further Processing')]//preceding-sibling::span")
    private WebElement acceptRecCount;
    @FindBy(xpath = "//span[contains(text(),'Records Returned for Correction')]//preceding-sibling::span")
    private WebElement rejectRecCount;
    @FindBy(xpath = "//tbody[1]/tr/td[3]")
    private List<WebElement> applicantName;
    @FindBy(xpath = "//tbody[1]/tr/td[4]")
    private List<WebElement> cutomerIdentityNum;
    @FindBy(xpath = "//tbody[1]/tr/td[2]")
    private List<WebElement> fileNumberList;
    @FindBy(xpath = "//table[1]/tbody[1]/tr/td[6]")
    private List<WebElement> rejectReason;
    @FindBy(xpath = "//ngb-pagination[1]/ul/li/a[contains(@aria-label, 'Next')]")
    private WebElement nextBtn;
    @FindBy(xpath = "(//ngb-pagination[1]/ul/li/a)[last()]")
    private  WebElement lastBtn;
    @FindBy(xpath = "//table[1]/tbody[1]/tr[1]/td[2]")
    private WebElement fileNumberPath;
    @FindBy(xpath = "//mat-icon[contains(text(),'expand_more')]")
    private WebElement expandMore;
    @FindBy(xpath = "//a[contains(text(),'Logout')]")
    private WebElement logOutBtn;
    @FindBy(xpath = "//h2[contains(text(),' Status of the Uploaded File ')]")
    private WebElement uploadedFileHeading;
    @FindBy(xpath = "//app-snackbar[1]/div[1]/div[1]/div[1]")
    private WebElement popupMsg;

    //Records uploaded tab
    @FindBy(xpath = "//span[contains(text(),'Records Uploaded')]")
    private WebElement recordsUploadedTab;
    @FindBy(xpath = "//span[contains(text(),'Records Accepted For Further Processing')]")
    private WebElement acceptedRecordTab;
    @FindBy(xpath = "//span[contains(text(),'Records Returned For Correction')]")
    private WebElement returnedRecordTab;

    //URN status tab
    @FindBy(xpath = "//span[contains(text(),'Records Pending For Validation by IME')]")
    private WebElement IMEPendingRecords;
    @FindBy(xpath = "//span[contains(text(),'Records for which URN Generated')]")
    private WebElement URNGenerated;
    @FindBy(xpath = "//span[contains(text(),'Records Returned Due To AUA Response')]")
    private WebElement AUAReturnedRecords;


    //Voter & Aadhar verification
    @FindBy(xpath = "//div[3]/div/div/div/a")
    private WebElement checkCred;
    @FindBy(xpath = "//input[@placeholder='Please enter Voter ID']")
    private WebElement voterIDField;
    @FindBy(xpath = "//span[contains(text(),'I Agree & Proceed')]")
    private WebElement voterIDProceedBtn;
    @FindBy(xpath = "//mat-radio-button/label/span[2]")
    private List<WebElement> mobileOnWeb;
    @FindBy(xpath = "//span[contains(text(),'Send OTP')]")
    private WebElement sendOTPBtn;
    @FindBy(xpath = "//input[@formcontrolname='otp']")
    private WebElement otpField;
    @FindBy(xpath = "//span[contains(text(),'Submit')]")
    private WebElement submitOTPBtn;
    @FindBy(xpath = "//div[@class='reg_form']/div[1]//input")
    private WebElement aadharEnterFields;
    @FindBy(xpath = "//h2[contains(text(),'Thank you')]")
    private WebElement thankYouPage;



    //global variables
    public List<String> applicantNameAccept = new ArrayList<>();
    public List<String> applicantNameReject = new ArrayList<>();
    public List<String> rejectReasonList = new ArrayList<>();
    public List<String> acceptCustomerNumber = new ArrayList<>();
    public List<String> rejectCustomerNumber = new ArrayList<>();
    public static String fileNumber;


//    SELECT matched_with,adv_status,aua_status,customer_stage_id FROM udhyam_assist.customer_detail where customer_identification_no = 'CW-AWC-2753783'








    public void login(String user) throws Exception {
        MFIFlowPages verify = new MFIFlowPages(driver);
        if(user.equalsIgnoreCase("MFI DA User")) {
            int logstep=1;
            int numberofFailure=0;
            CommonMethods.isLoadedAndClick(homeLoginBtn,driver);
            logInProxy loginproxy = (logInProxy)details.get(Constants.User.BANK_DA_USER_LOGIN);
            CommonMethods.isLoadedAndSendkeys(email,driver,loginproxy.getEmail());
            CommonMethods.isLoadedAndSendkeys(password,driver,loginproxy.getPassword());
            CommonMethods.sleep(15);
            CommonMethods.clickOnElementUsingJS(loginBtn,driver);
            logstep =log(logstep,"Verify Login functionality.");
            if(verify.loginSuccessfully()){
                Reporter.log("<br><Strong><font color=#008000>Pass</font></strong>");
            } else {
                Reporter.log("<br><Strong><font color=#FF0000>Fail</font></strong>");
                numberofFailure++;
            }
        }
    }

    public void logOutUser() throws InterruptedException {
        MFIFlowPages verify = new MFIFlowPages(driver);
        int logstep=1;
        int numberofFailure=0;
        CommonMethods.clickOnElementUsingJS(expandMore,driver);
        CommonMethods.isLoadedAndClick(logOutBtn,driver);
        logstep =log(logstep,"Verify Logout functionality.");
        if(verify.logoutUser()){
            Reporter.log("<br><Strong><font color=#008000>Pass</font></strong>");
        } else {
            Reporter.log("<br><Strong><font color=#FF0000>Fail</font></strong>");
            numberofFailure++;
        }
    }


    public void uploadFile(String filePath) throws InterruptedException, AWTException {
        int logstep=1;
        int numberofFailure=0;
        MFIFlowPages verify = new MFIFlowPages(driver);
        WebDriverWait wait = new WebDriverWait(driver,30);;
        String pageHead = pageHeading.getText().trim();
        System.out.println(pageHead);
        logstep = log(logstep,"Go to Bulk upload page for upload file");
        if (pageHead != "Bulk upload") {
            CommonMethods.clickOnElementUsingJS(bulkUploadBtn, driver);
            System.out.println("Go to bulk upload page");
        } else {
            System.out.println("already on bulk upload page");
        }

        /**
         * This is for upload file method
         */
        CommonMethods.sleep(3);
        CommonMethods.clickOnElementUsingJS(chooseFileBtn, driver);
        chooseFileBtn.sendKeys(filePath);
        CommonMethods.sleep(5);
        System.out.println("click on upload button");
        uploadFileBtn.click();
        if(verify.fileUploadSuccess()){
            Reporter.log("<br><Strong><font color=#008000>Pass</font></strong>");
        } else {
            Reporter.log("<br><Strong><font color=#FF0000>Fail</font></strong>");
            numberofFailure++;
        }
        logstep = log(logstep,"File is uploaded now and pending for success or reject");
        CommonMethods.refresh(driver);

        /**
         * This is for File Name and uploaded File is same
         */
        String trimPath = filePath.replace("D:\\Automatio Script\\UAP-Automationscript\\Resources\\", "");
        System.out.println("File Name of path selected --> "+trimPath);
        wait.until(ExpectedConditions.visibilityOf(fileName));
        String getFileName = fileName.getText();
        System.out.println("File name from uploaded successfully file -> " + fileName.getText());
        fileNumber = fileNumberPath.getText();
        if (trimPath.equalsIgnoreCase(getFileName)) {
            System.out.println("File name is matched");
            logstep = log(logstep,"Uploaded file & upload file in user is same");
        }
        else {
            System.out.println("File is not uploaded yet");
            numberofFailure++;

        }
        CommonMethods.sleep(3);
    }

    public int rejectSuccessCount() throws InterruptedException {
        int logstep=1;
        int count = 1;
        int recacptd = 0;
        int recrjctd = 0;
        do {
            try {
                count = 1;
                String recordsAcptd = recordsAccepted.getText().replace("file_download", "");
                recordsAcptd = recordsAcptd.replace("\n", "");
                String recordsrutd = recordsReturned.getText().replace("file_download", "");
                recordsrutd = recordsrutd.replace("\n", "");
                recacptd = Integer.parseInt(recordsAcptd);
                recrjctd = Integer.parseInt(recordsrutd);
                int sum = recacptd + recrjctd;
                System.out.println("Sum of accepted & rejected count --> " + sum);
                int uploadedCuunt = Integer.parseInt(recordsUploaded.getText());
                if (uploadedCuunt == sum) {
                    System.out.println("Records uploaded successfully count --> " + recordsAcptd);
                    System.out.println("Records rejected from validation --> " + recordsrutd);
                    logstep = log(logstep,"Record accepted count -> "+recordsAcptd + "/n Record rejected count -> "+recordsrutd);
                    count += 1;
                }
            }
            catch(Exception e){
                System.out.println("Records not matched");
                e.printStackTrace();
            }
            CommonMethods.refresh(driver);
        }
        while (count == 1);
        return recrjctd;

    }

    public int getAcceptedDetails() throws InterruptedException {
        int logstep=1;
        int acceptedCount = 0;
        if(pageHeading.getText().equalsIgnoreCase("Bulk Upload")) {
            CommonMethods.clickOnElementUsingJS(viewUploadRec, driver);
        }
        else {
            System.out.println("already on uploaded file record page");
        }


        /**
         * This method for get details from accepted record
         */
        CommonMethods.sleep(3);
        acceptedCount = Integer.parseInt(rejectRecCount.getText());
        System.out.println("Count of rejected records --> "+acceptedCount);
        logstep = log(logstep,"Accepted details get from view uploaded file");
        if (acceptedCount>0) {
            String nextbtn = lastBtn.getCssValue("class");
            int count = 1;
            do {
                try {
                    count = 1;
                    if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                        count += 1;
                    }
                    for (int i = 1; i < applicantName.size(); i++) {
                        for (WebElement nameEle : driver.findElements(By.xpath("//tbody[1]/tr["+(i+1)+"]/td[3]"))) {
                            applicantNameAccept.add(nameEle.getText());
                        }
                        for (WebElement nameEle1 : driver.findElements(By.xpath("//tbody[1]/tr["+(i+1)+"]/td[4]"))) {
                            acceptCustomerNumber.add(nameEle1.getText());
                            CommonMethods.sleep(3);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Element is not showing");
                    e.printStackTrace();
                }
                CommonMethods.clickOnElementUsingJS(nextBtn, driver);
                nextbtn = lastBtn.getAttribute("aria-disabled");
                CommonMethods.sleep(3);
            }
            while (count == 1);
            System.out.println("Accepted entry applicant name list -> " + applicantNameAccept);
        }
        else {
            System.out.println("Count is zero so not get any data");
        }
        return acceptedCount;
    }

    public int getRejctedDetails() throws InterruptedException {
        int logstep=1;
        int rejectedCount = 0;
        if(pageHeading.getText().equalsIgnoreCase("Bulk Upload")) {
            CommonMethods.clickOnElementUsingJS(viewUploadRec, driver);
        }
        else {
            System.out.println("already on uploaded file record page");
        }

        /**
         * This method for rejected count
         */
        CommonMethods.clickOnElementUsingJS(rejectRecCount,driver);
        System.out.println("Click on rejected records");
        rejectedCount = Integer.parseInt(rejectRecCount.getText());
        System.out.println("Count of rejected records --> "+rejectedCount);
        logstep = log(logstep,"Rejected details get from uploaded file");
        if (rejectedCount > 0) {
            String nextbtn = lastBtn.getCssValue("class");
            int count = 1;
            do {
                try {
                    count = 1;
                    if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                        count += 1;
                    }
                    for (int i = 1; i < applicantName.size(); i++){
                        for (WebElement nameEle2 : driver.findElements(By.xpath("//tbody[1]/tr["+(i+1)+"]/td[3]"))) {
                            applicantNameReject.add(nameEle2.getText());
                            CommonMethods.sleep(3);
                        }
                        for (WebElement nameEle1 : driver.findElements(By.xpath("//tbody[1]/tr["+(i+1)+"]/td[6]"))) {
                            rejectReasonList.add(nameEle1.getText());
                            CommonMethods.sleep(3);
                        }
                        for (WebElement nameEle1 : driver.findElements(By.xpath("//tbody[1]/tr["+(i+1)+"]/td[4]"))) {
                            rejectCustomerNumber.add(nameEle1.getText());
                            CommonMethods.sleep(3);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Element is not showing");
                    e.printStackTrace();
                }
                CommonMethods.clickOnElementUsingJS(nextBtn, driver);
                nextbtn = lastBtn.getAttribute("aria-disabled");
                CommonMethods.sleep(1);
            }
            while (count == 1);
            for (int i=0;i<applicantName.size();i++) {
                System.out.println("Rejected entry applicant name list -> " + applicantNameReject.get(i) + "\n and reasong of rejection is --> " + rejectReasonList.get(i));
            }
        }
        else {
            System.out.println("Count is zero so not get any data");
        }
        return rejectedCount;
    }

    /**
     * This method is for records uploaded tab data verify
     * @throws InterruptedException
     */
    public void recordsUploadedTab() throws InterruptedException {
        CommonMethods.clickOnElementUsingJS(recordStatusBtn,driver);
        String nextbtn = lastBtn.getCssValue("class");
        int count = 1;
        do {
            try {
                count = 1;
                if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                    count += 1;
                }
                for (int i = 1; i < fileNumberList.size(); i++) {
                    for (WebElement nameEle : driver.findElements(By.xpath("//tbody[1]/tr[" + (i + 1) + "]/td[2]"))) {
                        if (nameEle.getText().equalsIgnoreCase(fileNumber)){
                                if (cutomerIdentityNum.get(i+1).getText().equalsIgnoreCase(acceptCustomerNumber.get(i))){
                                    System.out.println("Customer number of Success from uploaded tab -->"+cutomerIdentityNum.get(i+1).getText());
                                    System.out.println("Cutomer number from uploaded tab --> "+acceptCustomerNumber.get(i));
                                } else if (cutomerIdentityNum.get(i+1).getText().equalsIgnoreCase(rejectCustomerNumber.get(i))){
                                System.out.println("Customer number of reject from uploaded tab -->"+cutomerIdentityNum.get(i+1).getText());
                                System.out.println("Cutomer number from uploaded tab --> "+rejectCustomerNumber.get(i));
                            }
                        }
                        else {
                            System.out.println("Uploaded records data is completed");
                            count += 1;
                        }
                    }
                }
            }
                catch(Exception e){
                    System.out.println("Element is not showing");
                    e.printStackTrace();
                }
                CommonMethods.clickOnElementUsingJS(nextBtn, driver);
                nextbtn = lastBtn.getAttribute("aria-disabled");
                CommonMethods.sleep(1);
            }
            while (count == 1) ;

    }

    /**
     * This method is for accepted record from records status section data verify
     * @throws InterruptedException
     */
    public void acceptedRecordTab() throws InterruptedException {
        CommonMethods.clickOnElementUsingJS(acceptedRecordTab,driver);
        String nextbtn = lastBtn.getCssValue("class");
        int count = 1;
        do {
            try {
                count = 1;
                if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                    count += 1;
                }
                for (int i = 1; i < fileNumberList.size(); i++) {
                    for (WebElement nameEle : driver.findElements(By.xpath("//tbody[1]/tr[" + (i + 1) + "]/td[2]"))) {
                        if (nameEle.getText().equalsIgnoreCase(fileNumber)){
                            if (cutomerIdentityNum.get(i+1).getText().equalsIgnoreCase(acceptCustomerNumber.get(i))){
                                System.out.println("Customer number of Success from uploaded tab -->"+cutomerIdentityNum.get(i+1).getText());
                                System.out.println("Cutomer number from uploaded tab --> "+acceptCustomerNumber.get(i));
                            }
                        }
                        else {
                            System.out.println("success record data is completed");
                            count += 1;
                        }
                    }
                }
            }
            catch(Exception e){
                System.out.println("Element is not showing");
                e.printStackTrace();
            }
            CommonMethods.clickOnElementUsingJS(nextBtn, driver);
            nextbtn = lastBtn.getAttribute("aria-disabled");
            CommonMethods.sleep(1);
        }
        while (count == 1) ;

    }

    /**
     * This method is for rejected record from Records status data verify
     * @throws InterruptedException
     */
    public void returnedRecordTab() throws InterruptedException {
        CommonMethods.clickOnElementUsingJS(returnedRecordTab,driver);
        String nextbtn = lastBtn.getCssValue("class");
        int count = 1;
        do {
            try {
                count = 1;
                if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                    count += 1;
                }
                for (int i = 1; i < fileNumberList.size(); i++) {
                    for (WebElement nameEle : driver.findElements(By.xpath("//tbody[1]/tr[" + (i + 1) + "]/td[2]"))) {
                        if (nameEle.getText().equalsIgnoreCase(fileNumber)){
                            if (cutomerIdentityNum.get(i+1).getText().equalsIgnoreCase(rejectCustomerNumber.get(i))){
                                System.out.println("Customer number of reject from uploaded tab -->"+cutomerIdentityNum.get(i+1).getText());
                                System.out.println("Cutomer number from uploaded tab --> "+rejectCustomerNumber.get(i));
                            }
                        }
                        else {
                            System.out.println("Rejected records data is completed");
                            count += 1;
                        }
                    }
                }
            }
            catch(Exception e){
                System.out.println("Element is not showing");
                e.printStackTrace();
            }
            CommonMethods.clickOnElementUsingJS(nextBtn, driver);
            nextbtn = lastBtn.getAttribute("aria-disabled");
            CommonMethods.sleep(1);
        }
        while (count == 1) ;

    }

    /**
     * This method is for records accepted for further processing from URN status data verify
     * @throws InterruptedException
     */
    public void acceptedForFurtherProcessTab() throws InterruptedException {
        CommonMethods.clickOnElementUsingJS(urnStatusBtn,driver);
        String nextbtn = lastBtn.getCssValue("class");
        int count = 1;
        do {
            try {
                count = 1;
                if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                    count += 1;
                }
                for (int i = 1; i < fileNumberList.size(); i++) {
                    for (WebElement nameEle : driver.findElements(By.xpath("//tbody[1]/tr[" + (i + 1) + "]/td[2]"))) {
                        if (nameEle.getText().equalsIgnoreCase(fileNumber)){
                            if (cutomerIdentityNum.get(i+1).getText().equalsIgnoreCase(acceptCustomerNumber.get(i))){
                                System.out.println("Customer number of accepted records -->"+cutomerIdentityNum.get(i+1).getText());
                                System.out.println("Cutomer number from uploaded tab --> "+acceptCustomerNumber.get(i));
                            }
                        }
                        else {
                            System.out.println("records accepted for further process data is completed");
                            count += 1;
                        }
                    }
                }
            }
            catch(Exception e){
                System.out.println("Element is not showing");
                e.printStackTrace();
            }
            CommonMethods.clickOnElementUsingJS(nextBtn, driver);
            nextbtn = lastBtn.getAttribute("aria-disabled");
            CommonMethods.sleep(1);
        }
        while (count == 1) ;
    }

    /**
     * This is the method for check URN generated status
     * @param name
     * @throws InterruptedException
     */
    public void URNgeneratedTab(String name) throws InterruptedException {
        MFIFlowPages verify = new MFIFlowPages(driver);
        WebDriverWait wait = new WebDriverWait(driver,300);;
        CommonMethods.clickOnElementUsingJS(urnStatusBtn,driver);
        String nextbtn = lastBtn.getCssValue("class");
        int count = 1;
        do {
            try {
                count = 1;
                if (!(null == nextbtn) || nextbtn.equalsIgnoreCase("true")) {
                    count += 1;
                }
                for (int i = 1; i < fileNumberList.size(); i++) {
                    for (WebElement nameEle : driver.findElements(By.xpath("//tbody[1]/tr[" + (i + 1) + "]/td[2]"))) {
                        if (nameEle.getText().equalsIgnoreCase(fileNumber)){
                            String string = applicantName.get(i+1).getText();
                            wait.until(d -> name.equalsIgnoreCase(string));
                            if(name.equalsIgnoreCase(string)){
                                String CIN = driver.findElements(By.xpath("//tbody[1]/tr["+(i+1)+"]/td[4]")).get(i+1).getText();
                                verify.stageIDCheck(CIN,1);

                            }
                        }
                        else {
                            System.out.println("records accepted for further process data is completed");
                            count += 1;
                        }
                    }
                }
            }
            catch(Exception e){
                System.out.println("Element is not showing");
                e.printStackTrace();
            }
            CommonMethods.clickOnElementUsingJS(nextBtn, driver);
            nextbtn = lastBtn.getAttribute("aria-disabled");
            CommonMethods.sleep(1);
        }
        while (count == 1) ;
    }


    /**
     * This method for MFI verification journey
     * @param excelFilePath
     * @param listOfApplicantName
     * @throws Exception
     */
    public void mfiVerification(String excelFilePath, List<String> listOfApplicantName) throws Exception {
        int logstep=1;
        int numberofFailure=0;
        ExcelFileReader exred = new ExcelFileReader(excelFilePath);
//        applicantNameAccept = Arrays.asList(new String[]{"Ratnesh Sonkar"});
        System.out.println(listOfApplicantName);
        System.out.println(exred.totalColumn("Sheet1"));
        System.out.println(exred.totalRow("Sheet1"));
        logstep = log(logstep,"Read excel file for compare success record and do flow for success record");
        for(int i=0;i<listOfApplicantName.size();i++){
            int maxIndex= exred.totalRow("Sheet1");
            for(int j=0;j<maxIndex;j++){
                String Data = exred.getData("Sheet1",j,1);
                if(Data.equals(listOfApplicantName.get(i))){
                    System.out.println(Data);
                    String voterID = exred.getData("Sheet1",j,2);
                    System.out.println("Voter ID of "+Data+" user --> "+voterID);
                    BigDecimal bd = new BigDecimal(exred.getData("Sheet1",j,3));
                    String mobile = String.valueOf(bd.longValue());
                    System.out.println("Mobile no of "+Data+" user --> "+mobile);
                    String aadharNo = exred.getData("Sheet1",j,0);
                    System.out.println("Aadhar no of "+Data+" user --> "+aadharNo);
                    List<String> result = CommonMethods.usingSubstringMethod(aadharNo,4);
                    System.out.println(result);
                    voterIdVerification(voterID,mobile,result,result);

                }
            }
        }
    }
    public void voterIdVerification(String voterID,String mobileNo,List<String> aadhar,List<String> confirmAadhar) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10000).toMillis());
        int numberofFailure=0;
        AESOracle encDctQuery = new AESOracle();
        CommonMethods.sleep(4);
        CommonMethods.clickOnElementUsingJS(checkCred,driver);
        CommonMethods.isLoadedAndSendkeys(voterIDField,driver,voterID);
        CommonMethods.clickOnElementUsingJS(voterIDProceedBtn,driver);
        CommonMethods.sleep(2);
        System.out.println("Message after enter & proceed on voter ID enter -> "+popupMsg.getText());
        if(popupMsg.getText().equalsIgnoreCase("Successfully Fetched Mobile number")){
            for(WebElement element : mobileOnWeb){
                if (element.getText().equals(mobileNo)){
                    System.out.println(element.getText() + " -> Web mobile is matched with -> "+mobileNo );
                    String max = "//span[contains(text(),'"+element.getText()+"')]";
                    CommonMethods.isElementPresentClick(By.xpath(max),driver);
                    CommonMethods.clickOnElementUsingJS(sendOTPBtn,driver);
                    CommonMethods.sleep(3);
//                    wait.until(d->otpField.isDisplayed());
                    String encMobile = CommonMethods.encryptionWithKey(mobileNo);
                    System.out.println("Encrypted Mobile no -> "+encMobile);
                    String query = ("select * from otp_service.otp_logging_details where mobile_no = '"+encMobile+"' order by id desc limit 1;");
                    System.out.println(query);
                    CommonMethods.sleep(3);
                    Object OTP = CommonMethods.getValuefromDB(query,"otp",connection);
                    System.out.println("OTP -> "+OTP);
                    String decOTP = encDctQuery.decriptWithKey((String) OTP);
                    System.out.println("Decrypted OTP --> "+decOTP);
                    CommonMethods.isLoadedAndSendkeys(otpField,driver,decOTP);
                    CommonMethods.clickOnElementUsingJS(submitOTPBtn,driver);
                    aadharVerification(aadhar,confirmAadhar);
                }
            }
        }
        else if (popupMsg.getText().equalsIgnoreCase("Voterid/Aadhar Already Verified")){
            if(thankYouPage.isDisplayed()){
                System.out.println("Aadhar verification & Voter ID verification is already completed");
            }
            else {
                System.out.println("Already completed verification for Voter ID");
                aadharVerification(aadhar,confirmAadhar);
            }
        }else if (popupMsg.getText().equalsIgnoreCase("Voterid is invalid")){
            System.out.println("File is not uploaded yet or another issue");
            numberofFailure++;
        }
    }
    public void aadharVerification(List<String> aadhar,List<String> confirmAadhar) throws InterruptedException {
        for(int i=0; i<aadhar.size(); i++){
            System.out.println(aadhar.size());

            driver.findElement(By.xpath("//input[@formcontrolname='aadhar"+(i+1)+"']")).sendKeys(aadhar.get(i));
            System.out.println("//input[@formcontrolname='aadhar"+(i+1)+"']");

        }
        for(int j=0; j<confirmAadhar.size(); j++){

            driver.findElement(By.xpath("//input[@formcontrolname='aadharVerify"+(j+1)+"']")).sendKeys(confirmAadhar.get(j));
            System.out.println("//input[@formcontrolname='aadharVerify"+(j+1)+"']");

        }
        CommonMethods.clickOnElementUsingJS(submitOTPBtn,driver);
        System.out.println("After completed journey -> "+popupMsg.getText());
        CommonMethods.sleep(5);
        driver.get(baseUrl);
        CommonMethods.sleep(3);
    }

}
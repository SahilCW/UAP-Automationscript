package com.capitaworld.Pages;

import com.capitaworld.commonutil.CommonMethods;
import com.capitaworld.config.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class MFIFlowPages extends AbstractPage {
        public MFIFlowPages(WebDriver driver) {
            super(driver);
            // TODO Auto-generated constructor stub
        }

//
    @FindBy(xpath = "//app-snackbar[1]/div[1]/div[1]/div[1]")
    private WebElement popupMsg;


    /**
     * Method for verification of Login user
      * @return
     * @throws InterruptedException
     */
    public boolean loginSuccessfully() throws InterruptedException {
        boolean ans = false;
        CommonMethods.sleep(1);
        if (popupMsg.getText().trim().equalsIgnoreCase("Login Successfully")){
            System.out.println("User is login succesfully in user");
            Reporter.log("<br/>"+popupMsg.getText().trim());
            ans = true;
        }
        else{
            System.out.println("User is getting error for --> "+popupMsg.getText().trim());
            Reporter.log("<br/ User is not able to login for --> "+popupMsg.getText().trim());
            ans = false;
        }
        System.out.println("Returning from verification Method of Login successfully --> "+ans);
        return ans;
    }

    /**
     * Method for verification of Login user
     * @return
     * @throws InterruptedException
     */
    public boolean logoutUser() throws InterruptedException {
        boolean ans = false;
        CommonMethods.sleep(1);
        if (popupMsg.getText().trim().equalsIgnoreCase("Logout Successfully")){
            System.out.println("User is logout succesfully from user");
            Reporter.log("<br/>"+popupMsg.getText().trim());
            ans = true;
        }
        else{
            System.out.println("User is getting error for --> "+popupMsg.getText().trim());
            Reporter.log("<br/ User is not able to logout --> "+popupMsg.getText().trim());
            ans = false;
        }
        System.out.println("Returning from verification Method of Logout successfully --> "+ans);
        return ans;
    }

    /**
     * Method for verification of Login user
     * @return
     * @throws InterruptedException
     */
    public boolean fileUploadSuccess() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver,30);
        boolean ans = false;
        CommonMethods.sleep(1);
        wait.until(ExpectedConditions.visibilityOf(popupMsg));
        if (popupMsg.getText().trim().equalsIgnoreCase("Your file is being processed. Please check back later")){
            System.out.println("File uploaded successfully");
            Reporter.log("<br/>"+popupMsg.getText().trim());
            ans = true;
        }
        else{
            System.out.println("User is getting error for --> "+popupMsg.getText().trim());
            Reporter.log("<br/ File is not upload because --> "+popupMsg.getText().trim());
            ans = false;
        }
        System.out.println("Returning from verification Method of File upload successfully --> "+ans);
        return ans;
    }


    /**
     * This method is for check stage ID
     * @param CIN
     * @param stageID
     * @return
     */
    public boolean stageIDCheck(String CIN,Object stageID) {
        boolean ans = false;
        Object response = CommonMethods.getValuefromDB("SELECT stage_id FROM udhyam_assist.applicantion_status where customer_id = '"+CIN+"'","customer_stage_id",connection);
        int count = 1;
        do {
            count = 1;
            if(response == null || !response.equals(stageID)){
                count+=1;
                System.out.println("Stage ID is not updated till");
                Reporter.log("<br/ Stage is not updated still");
            } else {
                System.out.println("Stage ID is updated as per mentioned");
                Reporter.log("<br/ Stage ID is updated as per mentioned");
                ans = true;
            }
        }
        while (count == 1);
        return ans;
    }


    public boolean auaStatusCheck(String CIN,Object stageID) {
        boolean ans = false;
        Object response = CommonMethods.getValuefromDB("SELECT aua_status FROM udhyam_assist.customer_detail where customer_identification_no = '"+CIN+"'","customer_stage_id",connection);
        int count = 1;
        do {
            count = 1;
            if(response == null || !response.equals(stageID)){
                count+=1;
                System.out.println("AUA Status is still not updated");
                Reporter.log("<br/ Stage is not updated still");
            } else {
                System.out.println("Stage ID is updated as per mentioned");
                Reporter.log("<br/ Stage ID is updated as per mentioned");
                ans = true;
            }
        }
        while (count == 1);
        return ans;
    }

    public boolean commonComparison(Object data,Object data2){
        boolean ans = false;
        int count = 1;
        if (data.equals(data2)){
            count+=1;
            System.out.println(data + " Data one matched with second data" + data2);
            Reporter.log("<br/"+ data + " Data one matched with second data" + data2);
            ans = true;
        }
        else {
            System.out.println(data + " Data one is not matched with second data" + data2);
            Reporter.log("<br/"+ data + " Data one is not matched with second data" + data2);
        }
        return ans;
    }


}

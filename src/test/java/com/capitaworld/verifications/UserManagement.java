package com.capitaworld.verifications;

import com.capitaworld.Pages.MFIFlowPages;
import com.capitaworld.commonutil.CommonMethods;
import com.capitaworld.commonutil.DataUtil;
import com.capitaworld.config.AESOracle;
import com.capitaworld.config.Config;
import com.capitaworld.indexPages.flowCommonMethods;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.Map;

public class UserManagement extends Config {
    Map< String,Object> details;
    DataUtil util =null;

    public UserManagement()
    {
        util=DataUtil.getInstance();
        details=util.getDetails();
    }

    @Test
    public void validationCheck() throws Exception {
        int logStep=1;
        int numberofFailure=0;
        MFIFlowPages verify = new MFIFlowPages(driver);
        flowCommonMethods lg = new flowCommonMethods(driver);
        lg.login("MFI DA User");
        String filePath = "D:\\Automatio Script\\UAP-Automationscript\\Resources\\Scenario 16 MFI flow (1).xlsb.enc";
        lg.uploadFile(filePath);
        int reject = lg.rejectSuccessCount();
        logStep=log(logStep, "Verify rejected user details.");
        if (verify.commonComparison(reject,2)){
            Reporter.log("<br><Strong><font color=#008000>Pass</font></strong>");
        } else {
            Reporter.log("<br><Strong><font color=#FF0000>Fail</font></strong>");
            numberofFailure++;
        }
        lg.getAcceptedDetails();
        lg.getRejctedDetails();
    }

    @Test
    public void statusRecord() throws Exception {
        int logStep=1;
        int numberofFailure=0;
        MFIFlowPages verify = new MFIFlowPages(driver);
        flowCommonMethods lg = new flowCommonMethods(driver);
        lg.login("MFI DA User");
        String filePath = "D:\\Automatio Script\\UAP-Automationscript\\Resources\\Scenario 16 MFI flow (1).xlsb.enc";
        lg.uploadFile(filePath);
        int reject = lg.rejectSuccessCount();
        logStep=log(logStep, "Verify rejected user details.");
        if (verify.commonComparison(reject,2)){
            Reporter.log("<br><Strong><font color=#008000>Pass</font></strong>");
        } else {
            Reporter.log("<br><Strong><font color=#FF0000>Fail</font></strong>");
            numberofFailure++;
        }
        lg.getAcceptedDetails();
        lg.getRejctedDetails();

    }

}

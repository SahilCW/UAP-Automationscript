package com.capitaworld.commonutil;



import com.capitaworld.proxy.MethodDetails;
import com.capitaworld.proxy.logInProxy;

import java.util.HashMap;
import java.util.Map;

public class DataUtil {

    private static DataUtil dataUtil = null;

    private Map<String, Object> details = new HashMap<String, Object>();

    public static DataUtil getInstance() {
        if (dataUtil == null) {
            dataUtil = new DataUtil();
        }
        return dataUtil;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    private DataUtil() {

        // Set details of Site.
        addUserDetails();
    }

    private void addUserDetails()
    {
        details.put(Constants.User.TESTCASE_1_VALIDATION_CHECK, new MethodDetails("UAP_VALIDATUIN_CHECK_001", Constants.AUDITTRAIL_PRESENT));
        details.put(Constants.User.BANK_DA_USER_LOGIN, new logInProxy("prakash.shrimali@onlinepsbloans.com","admin@123"));


    }


}

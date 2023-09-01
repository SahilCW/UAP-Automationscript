package com.capitaworld.verifications;

import com.capitaworld.commonutil.DataUtil;
import com.capitaworld.config.Config;
import com.capitaworld.indexPages.loginUser;
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
    public void testuser() throws InterruptedException {
        System.out.println("Hello");
        loginUser lg = new loginUser(driver);
        lg.login("indian banker");

    }
}

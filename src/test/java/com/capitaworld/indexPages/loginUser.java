package com.capitaworld.indexPages;
import com.capitaworld.proxy.logInProxy;
import com.capitaworld.commonutil.CommonMethods;
import com.capitaworld.commonutil.Constants;
import com.capitaworld.commonutil.DataUtil;
import com.capitaworld.config.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


import java.util.Map;

public class loginUser extends AbstractPage {
    Map<String, Object> details;
    DataUtil util = null;

    public loginUser(WebDriver driver) {
        super(driver);
        System.out.println("Driver instance created---" + driver.getTitle());
        util = DataUtil.getInstance();
        details = util.getDetails();
        // TODO Auto-generated constructor stub
    }

    //login user
    @FindBy(xpath = "//span[contains(text(),'Login')]")
    private WebElement homeLoginBtn;





    public void login(String user) throws InterruptedException {
        if(user.equalsIgnoreCase("Bank DA User")) {
            CommonMethods.isLoadedAndClick(homeLoginBtn,driver);

        }

    }

}

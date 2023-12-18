package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.api.LibraryAPI_Util;
import com.library.utility.api.LibraryTestBase;
import com.library.utility.db.DB_Util;
import com.library.utility.ui.BrowserUtil;
import com.library.utility.ui.Driver;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.Map;

public class US04_StepDefs extends LibraryTestBase {

    String userId;
    LoginPage loginPage = new LoginPage();
    BookPage bookPage = new BookPage();

    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {

        userId = validatableResponse.extract().jsonPath().getString("user_id");
        DB_Util.runQuery("select * from users\n" +
                "where id = " + userId);
        Map<String, Object> userDataFromDB = DB_Util.getRowMap(1);
        System.out.println("userDataFromDB = " + userDataFromDB);
        Map<String, Object> userDataFromApi = LibraryAPI_Util.getUserByID(userId);
        System.out.println("userDataFromApi = " + userDataFromApi);
        Assert.assertEquals(userDataFromApi, userDataFromDB);

    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        //String email = LibraryAPI_Util.getUserByID(userId).get("email").toString();
        DB_Util.runQuery("select email from users\n" +
                "where id =" + userId);
        String email = DB_Util.getFirstRowFirstColumn();
        String password = "libraryUser";
        loginPage.login(email, password);
        BrowserUtil.waitFor(2);
        String expectedUrl = "https://library2.cydeo.com/#dashboard";
        String actualUrl = Driver.getDriver().getCurrentUrl();
        Assert.assertEquals(expectedUrl, actualUrl);

    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {
        String actualUserName = bookPage.accountHolderName.getText();
        //String expectedUserName = LibraryAPI_Util.getUserByID(userId).get("full_name").toString();
        DB_Util.runQuery("select full_name from users\n" +
                "where id = " + userId);
        String expectedUserName = DB_Util.getFirstRowFirstColumn();
        Assert.assertEquals(expectedUserName, actualUserName);
    }

}

package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.api.LibraryAPI_Util;
import com.library.utility.api.LibraryTestBase;
import com.library.utility.db.DB_Util;
import com.library.utility.ui.BrowserUtil;
import groovy.util.MapEntry;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class US03_StepDefs extends LibraryTestBase {

    LoginPage loginPage = new LoginPage();

    BookPage bookPage = new BookPage();

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType) {
       request.contentType(contentType);
    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String requestBody) {
        request.formParams(LibraryAPI_Util.getRandomBookMap());
    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response = request.when().post(endpoint).prettyPeek();
        validatableResponse = response.then();
    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String expectedField, String expectedValue) {
        validatableResponse.body(expectedField,is(expectedValue));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {

        validatableResponse.body(path, is(notNullValue()));

    }
    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String role) {
        loginPage.login(role);
    }
    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String pageName) {
        bookPage.navigateModule(pageName);
    }
    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {
        //value of book id as string
        String bookId = validatableResponse.extract().jsonPath().getString("book_id");

        //run query to get all book information for String bookId
        DB_Util.runQuery("select * from books where id ="+bookId);
        //stored query result in a map
        Map<String, Object> bookDataDBforAPI = DB_Util.getRowMap(1);
        //made a new get request to endpoint book id
        Map<String, Object> bookDataAPI = LibraryAPI_Util.getBookByID(bookId);

        //ran new query with specific info that match UI info
        DB_Util.runQuery("select name, isbn, year, author from books where id="+bookId);
        //stored query result in a map
        Map<String, Object> bookDataDBforUI = DB_Util.getRowMap(1);
        //ran query to retrieve value of book category id
        DB_Util.runQuery("select name from book_categories where id ="+bookDataDBforAPI.get("book_category_id"));
        //stored book category id as String
        String bookCategoryId = DB_Util.getFirstRowFirstColumn();
        //put the book category in DBforUI map with key "category" to match UI and the value from previous query
        bookDataDBforUI.put("category",bookCategoryId);

        BrowserUtil.waitForVisibility(bookPage.search).sendKeys(bookDataDBforAPI.get("author").toString());
        //stored UI book info in map from custom method
        Map<String, Object> bookDataUI = bookPage.mapOfBookInfo();


        Assert.assertEquals(bookDataAPI,bookDataDBforAPI);
        Assert.assertEquals(bookDataDBforUI,bookDataUI);




    }

}

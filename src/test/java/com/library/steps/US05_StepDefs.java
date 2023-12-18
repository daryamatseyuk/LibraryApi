package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.api.LibraryAPI_Util;
import com.library.utility.api.LibraryTestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class US05_StepDefs extends LibraryTestBase {

    String token;
    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
        token = LibraryAPI_Util.getToken(email,password);
    }

    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        request.formParam("token", token)
                .baseUri(ConfigurationReader.getProperty("library.baseUri"));
    }


}

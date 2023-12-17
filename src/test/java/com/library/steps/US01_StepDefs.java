package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class US01_StepDefs {

    RequestSpecification request;
    Response response;
    ValidatableResponse validatableResponse;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        request = given().log().all()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));

    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {

        request.accept(contentType);

    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {

        response = request.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
        validatableResponse = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {

        validatableResponse.statusCode(statusCode).log().all();

    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {

        validatableResponse.contentType(contentType);

    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {

        validatableResponse.body(path, everyItem(notNullValue()));

    }


}

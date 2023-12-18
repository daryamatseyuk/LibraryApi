package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.api.LibraryAPI_Util;
import com.library.utility.api.LibraryTestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class US01_StepDefs extends LibraryTestBase {



    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        request = given().log().all()
                .header("x-library-token", LibraryAPI_Util.getToken(userType)).baseUri(ConfigurationReader.getProperty("library.baseUri"));

    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {

        request.accept(contentType);

    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {

        response = request.when().get(endpoint).prettyPeek();
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
    @Then("{string} fields should not be null")
    public void fields_should_not_be_null(String path) {

        validatableResponse.body(path, everyItem(notNullValue()));

    }


}

package com.library.steps;

import com.library.utility.api.LibraryTestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class US02_StepDefs extends LibraryTestBase {

    String expectedID;

    @Given("Path param is {string}")
    public void path_param_is(String id) {
     request.pathParam("id",id);

     expectedID = id;

    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String path) {

        validatableResponse.body(path, equalTo(expectedID));
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> expectedFields) {

        expectedFields.forEach(p -> validatableResponse.body(p, notNullValue()));

    }

}

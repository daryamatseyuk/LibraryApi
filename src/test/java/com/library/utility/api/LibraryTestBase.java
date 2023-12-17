package com.library.utility.api;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public abstract class LibraryTestBase {

   protected static RequestSpecification request;
   protected static Response response;
   protected static ValidatableResponse validatableResponse;


}

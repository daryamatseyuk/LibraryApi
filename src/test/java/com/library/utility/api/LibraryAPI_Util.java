package com.library.utility.api;

import com.github.javafaker.Faker;
import com.library.utility.ConfigurationReader;
import io.restassured.http.ContentType;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LibraryAPI_Util {


    /**
     * Return TOKEN as String by using provided username from /token endpoint
     *
     * @param userType
     * @return
     */
    public static String getToken(String userType) {


        String email = ConfigurationReader.getProperty(userType + "_username");
        String password = ConfigurationReader.getProperty(userType + "_password");


        return getToken(email, password);


    }

    public static String getToken(String email, String password) {


        return given()
                .contentType(ContentType.URLENC)
                .formParam("email", email)
                .formParam("password", password).
                when()
                .post(ConfigurationReader.getProperty("library.baseUri") + "/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");


    }

    public static Map<String, Object> getRandomDataMap(String dataType) {

        Faker faker = new Faker();
        Map<String, Object> dataMap = new LinkedHashMap<>();

        switch (dataType.toLowerCase()) {
            case "book":
                String randomBookName = faker.book().title() + faker.number().numberBetween(0, 10);
                dataMap.put("name", randomBookName);
                dataMap.put("isbn", faker.code().isbn10());
                dataMap.put("year", faker.number().numberBetween(1000, 2021));
                dataMap.put("author", faker.book().author());
                dataMap.put("book_category_id", faker.number().numberBetween(1, 20));  // in library app valid category_id is 1-20
                dataMap.put("description", faker.chuckNorris().fact());
                break;
            case "user":
                String fullName = faker.name().fullName();
                String email = faker.name().firstName()+ faker.number().numberBetween(1, 100) + "@library";
                System.out.println(email);
                dataMap.put("full_name", fullName);
                dataMap.put("email", email);
                dataMap.put("password", "libraryUser");
                // 2 is librarian as role
                dataMap.put("user_group_id", 2);
                dataMap.put("status", "ACTIVE");
                dataMap.put("start_date", "2023-03-11");
                dataMap.put("end_date", "2024-03-11");
                dataMap.put("address", faker.address().cityName());

        }


        return dataMap;
    }

//    public static Map<String, Object> getRandomUserMap() {
//
//        Faker faker = new Faker();
//        Map<String, Object> userMap = new LinkedHashMap<>();
//        String fullName = faker.name().fullName();
//        String email = fullName.substring(0, fullName.indexOf(" ")) + "@library";
//        System.out.println(email);
//        userMap.put("full_name", fullName);
//        userMap.put("email", email);
//        userMap.put("password", "libraryUser");
//        // 2 is librarian as role
//        userMap.put("user_group_id", 2);
//        userMap.put("status", "ACTIVE");
//        userMap.put("start_date", "2023-03-11");
//        userMap.put("end_date", "2024-03-11");
//        userMap.put("address", faker.address().cityName());
//
//        return userMap;
//    }

    public static Map<String, Object> getBookByID(String id) {
        return given()
                .accept(ContentType.JSON)
                .header("x-library-token", getToken("librarian"))
                .pathParam("id", id)
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_book_by_id/{id}")
                .then().statusCode(200)
                .extract().body().as(Map.class);
    }

    public static Map<String, Object> getUserByID(String id) {
        return given()
                .accept(ContentType.JSON)
                .header("x-library-token", getToken("librarian"))
                .pathParam("id", id)
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_user_by_id/{id}")
                .then().statusCode(200)
                .extract().body().as(Map.class);
    }


}

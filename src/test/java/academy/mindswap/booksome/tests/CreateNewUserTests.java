package academy.mindswap.booksome.tests;

import academy.mindswap.booksome.tests.util.UserUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreateNewUserTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    void createUser() {
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(UserUtil.createUserRequest())
                .when()
                .post("/api/v1/users")
                .then()
                .extract().response();

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(response.jsonPath().getString("id").isEmpty());
        Assertions.assertNull(response.jsonPath().getString("favoriteBooksId"));
        Assertions.assertNull(response.jsonPath().getString("readBooksId"));
        Assertions.assertNull(response.jsonPath().getString("roles"));
    }

    @Test
    void createUserWithRepeatedEmailShouldNotBeAllowed(){
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(UserUtil.createUserRequest("qwerty@gmail.com"))
                .when()
                .post("/api/v1/users")
                .then()
                .extract().response();

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(response.jsonPath().getString("id").isEmpty());
        Assertions.assertNull(response.jsonPath().getString("favoriteBooksId"));
        Assertions.assertNull(response.jsonPath().getString("readBooksId"));
        Assertions.assertNull(response.jsonPath().getString("roles"));

        Response repeatedResponse = given()
                .header("Content-Type", "application/json")
                .and()
                .body(UserUtil.createUserRequest("qwerty@gmail.com"))
                .when()
                .post("/api/v1/users")
                .then()
                .extract().response();

        Assertions.assertEquals(400, repeatedResponse.statusCode());

    }
    @Test
    void createUserWithMissingInformationShouldNotBeAllowed() {
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(UserUtil.createUserRequestWithMissingInformation())
                .post("/api/v1/users")
                .then()
                .extract().response();

        Assertions.assertEquals(400, response.statusCode());
    }


}

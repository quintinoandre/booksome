package academy.mindswap.booksome;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class CreateNewUserTests {

    private static String requestBody = "{\n" +
           " \"name\" :\"José Silva\", \n" +
            " \"email\" :\"1zedasilva@gmail.com\", \n" +
            " \"password\" :\"1234\" \n }";


    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void createUser(){
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(requestBody)
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






}

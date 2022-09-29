package academy.mindswap.booksome.tests;

import academy.mindswap.booksome.tests.util.UserUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BooksTests {
    private static String authToken;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        authToken = UserUtil.login();
    }


    @ParameterizedTest
    @ValueSource(strings = {"laranja", "jobs"})
    void deleteFavoriteBookFromList (String title){
        Response response = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/v1/books?title=" + title)
                .then()
                .extract().response();

        Assertions.assertTrue(response.jsonPath().getList("$").size() >= 2);

        List<Object> bookList = response.jsonPath().getList("$");
        HashMap book1 = (HashMap) bookList.get(0);
        HashMap book2 = (HashMap) bookList.get(1);
        String isbn1 = book1.get("isbn").toString();
        String isbn2 = book2.get("isbn").toString();

        Response saveBookResponse = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .put("/api/v1/users/book/" + isbn1 +"/favorites")
                .then()
                .extract().response();
        Assertions.assertEquals(200, saveBookResponse.statusCode());

        Response saveBookResponse1 = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .put("/api/v1/users/book/" + isbn2 +"/favorites")
                .then()
                .extract().response();
        Assertions.assertEquals(200, saveBookResponse1.statusCode());

        Response findFavoriteBooksResponse = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/v1/users/favoritebooks")
                .then()
                .extract().response();

        Assertions.assertEquals(200, findFavoriteBooksResponse.statusCode());
        Assertions.assertFalse(response.jsonPath().getList("favoriteBooksId").isEmpty());

        var favBookList = findFavoriteBooksResponse.jsonPath().getList("$");
        HashMap favBook1 = (HashMap) favBookList.get(0);
        String favBookId1 = favBook1.get("id").toString();

        Response findOneFavoriteBooksResponse = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/v1/users/favoritebook/" + favBookId1)
                .then()
                .extract().response();
        Assertions.assertEquals(200, findOneFavoriteBooksResponse.statusCode());
        Assertions.assertFalse(response.jsonPath().getString("id").isEmpty());

        Response deleteOneBookFromFavResponse = given()
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete("/api/v1/users/book/" + favBookId1 + "/favorites")
                .then()
                .extract().response();

            Assertions.assertEquals(200, deleteOneBookFromFavResponse.statusCode());
    }
}

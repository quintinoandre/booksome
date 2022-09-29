package academy.mindswap.booksome.tests.util;

import academy.mindswap.booksome.util.role.RoleTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;
import static io.restassured.RestAssured.given;


public final class UserUtil {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCreationRequest{
        private String name;
        private String email;
        private String password;
        private List<RoleTypes> roles;
    }

    public static UserCreationRequest createUserRequest(){
        String email = RandomStringUtils.randomAlphabetic(10);
        var requestBody = UserCreationRequest.builder()
                .name("José da Silva")
                .email(email + "@gmail.com")
                .password("1234")
                .build();

        return requestBody;
    }

    public static UserCreationRequest createUserRequest(String email){
        var requestBody = UserCreationRequest.builder()
                .name("José da Silva")
                .email(email)
                .password("1234")
                .build();

        return requestBody;
    }

    public static UserCreationRequest createUserRequestWithMissingInformation(){
        String email = RandomStringUtils.randomAlphabetic(10);
        var requestBody = UserCreationRequest.builder()
                .email(email + "@gmail.com")
                .password("1234")
                .build();

        return requestBody;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserWithRoleRequest{
        @JsonProperty("username")
        private String email;
        private String password;
        private List<RoleTypes> roles;
    }


    public static UserWithRoleRequest userToExtractToken(){
        var requestBody = UserWithRoleRequest.builder()
                .email("lygia.garrido@mindswap.academy")
                .password("1234")
                .build();
        return requestBody;
    }

    public static String login(){
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(UserUtil.userToExtractToken())
                .when()
                .post("api/v1/authenticate")
                .then()
                .extract().response();

        String token = response.jsonPath().getString("token");

        Response response1 = given()
                .header("isRefreshToken", "true")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("api/v1/refreshtoken")
                .then()
                .extract().response();

        return response1.jsonPath().getString("token");
    }
}


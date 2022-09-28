package academy.mindswap.booksome.tests.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;


public final class UserUtil {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCreationRequest{
        private String name;
        private String email;
        private String password;
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



}


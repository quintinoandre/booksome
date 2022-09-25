package academy.mindswap.booksome.exception.user;

public final class UserExceptionMessage {
    private UserExceptionMessage() {
    }

    public static final String USER_NOT_FOUND = "user not found";
    public static final String USERS_NOT_FOUND = "no user found";
    public static final String USER_NULL = "user object is null";
    public static final String EMAIL_ALREADY_EXISTS = "email already exists";
}

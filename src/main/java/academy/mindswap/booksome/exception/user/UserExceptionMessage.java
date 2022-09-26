package academy.mindswap.booksome.exception.user;

public final class UserExceptionMessage {
    private UserExceptionMessage() {
    }

    public static final String USER_NOT_FOUND = "User not found.";
    public static final String USERS_NOT_FOUND = "No user found.";
    public static final String USER_NULL = "User object is null.";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists.";
    public static final String USER_ID_NULL = "User id is null.";
    public static final String ROLES_NULL = "Roles object is null.";

    public static final String ALREADY_FAVORITE = "This book is already one of your favorites.";
}

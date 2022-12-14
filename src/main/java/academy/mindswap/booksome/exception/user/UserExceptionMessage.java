package academy.mindswap.booksome.exception.user;

public final class UserExceptionMessage {
    private UserExceptionMessage() {
    }

    public static final String USER_NOT_FOUND = "User not found.";
    public static final String USERS_NOT_FOUND = "No user found.";
    public static final String USER_NULL = "User cannot be null.";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists.";
    public static final String USER_ID_NULL = "User id cannot be null.";
    public static final String ROLES_NULL = "Roles object cannot be null.";
    public static final String ALREADY_FAVORITE = "This book is already one of your favorites.";
    public static final String ALREADY_READ = "You have already marked this book as read.";
    public static final String INVALID_NAME = "Please insert a valid name (eg. Brad Pitt).";
    public static final String NO_FAVORITE = "This book is not one of your favorites.";
    public static final String NO_READ = "You have not read this book yet.";
}

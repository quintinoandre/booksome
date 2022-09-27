package academy.mindswap.booksome.dto;

public final class DtoValidationMessage {
    private DtoValidationMessage() {
    }

    public static final String TITLE_MANDATORY = "Title is mandatory.";
    public static final String ISBN_MANDATORY = "ISBN is mandatory.";
    public static final String AUTHOR_MANDATORY = "Author is mandatory.";
    public static final String CATEGORY_MANDATORY = "Category is mandatory.";
    public static final String DESCRIPTION_MANDATORY = "Description is mandatory.";
    public static final String PUBLISHED_DATE_MANDATORY = "Published date is mandatory.";
    public static final String PUBLISHER_MANDATORY = "Publisher is mandatory.";
    public static final String NAME_MANDATORY = "Name is mandatory.";
    public static final String EMAIL_MANDATORY = "Email must be valid.";
    public static final String EMAIL_NOT_BLANK = "Email must not be blank.";
    public static final String LETTERS_ONLY = "^[a-zA-Z]+((\\s{1}[a-zA-Z]+)+)?";
    public static final String ROLE_MANDATORY = "At least one role is mandatory.";
    public static final String USERNAME_MANDATORY = "Username is mandatory.";
    public static final String PASSWORD_MANDATORY = "Password is mandatory.";
}

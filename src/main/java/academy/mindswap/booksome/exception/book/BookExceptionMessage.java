package academy.mindswap.booksome.exception.book;

public final class BookExceptionMessage {
    private BookExceptionMessage() {
    }

    public static final String ALL_PARAMS_NULL = "At least one parameter is required.";
    public static final String INVALID_PUBLISHED_DATE = "Please insert a valid book published date (eg. 2011-05-13).";
    public static final String INVALID_ISBN = "Please insert a valid book ISBN (eg. 9789722326018 or 9722326015).";
    public static final String BOOK_NULL = "Book cannot be null.";
    public static final String ISBN_ALREADY_EXISTS = "ISBN already exists.";
    public static final String BOOK_ISBN_NULL = "Book ISBN cannot be null.";
    public static final String BOOK_ID_NULL = "Book id cannot be null.";
    public static final String BOOK_NOT_FOUND = "Book not found.";
    public static final String BOOKS_NOT_FOUND = "No book found.";
    public static final String BOOK_EXISTS_IN_USER = "This book cannot be deleted because one or more users have it " +
            "in their favorite or read list";
}

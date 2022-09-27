package academy.mindswap.booksome.exception.book;

public final class BookExceptionMessage {
    private BookExceptionMessage() {
    }

    public static final String ALL_PARAMS_NULL = "At least one parameter is required.";
    public static final String INVALID_TITLE = "Please insert a valid book title (eg. Harry Potter).";
    public static final String INVALID_AUTHOR = "Please insert a valid book author (eg. J. K. Rowling).";
    public static final String INVALID_CATEGORY = "Please insert a valid book category (eg. Fiction).";
    public static final String INVALID_ISBN = "Please insert a valid book ISBN (eg. 9789722325332).";
    public static final String BOOK_NULL = "Book cannot be null.";
    public static final String ISBN_ALREADY_EXISTS = "ISBN already exists.";
    public static final String BOOK_ISBN_NULL = "Book ISBN cannot be null.";
    public static final String BOOK_ID_NULL = "Book id cannot be null.";
    public static final String BOOK_NOT_FOUND = "Book not found.";
    public static final String BOOKS_NOT_FOUND = "No book found.";
}

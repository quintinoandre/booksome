package academy.mindswap.booksome.controller.book;

public final class BookControllerConstant {
    private BookControllerConstant() {
    }

    static final String TITLE = "title";
    static final String AUTHORS = "authors";
    static final String CATEGORY = "category";
    static final String ISBN = "isbn";
    static final String LETTERS_ONLY = "^[a-zA-Z]+((\\s{1}[a-zA-Z]+)+)?";
    static final String NUMBERS_ONLY = "^\\d+$";
}

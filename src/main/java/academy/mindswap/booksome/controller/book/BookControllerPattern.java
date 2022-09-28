package academy.mindswap.booksome.controller.book;

public final class BookControllerPattern {
    private BookControllerPattern() {
    }

    static final String LETTERS_ONLY = "^[a-zA-Z]+((\\s{1}[a-zA-Z]+)+)?";
    static final String NUMBERS_ONLY = "^\\d+$";
}

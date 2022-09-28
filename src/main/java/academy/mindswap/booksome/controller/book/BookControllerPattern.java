package academy.mindswap.booksome.controller.book;

public final class BookControllerPattern {
    private BookControllerPattern() {
    }

    static final String LETTERS_ONLY = "^[A-Za-zÀ-ü]+((\\s{1}[A-Za-zÀ-ü]+)+)?";
    static final String NUMBERS_ONLY = "^\\d+$";
}

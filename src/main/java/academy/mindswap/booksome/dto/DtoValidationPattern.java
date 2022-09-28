package academy.mindswap.booksome.dto;

public final class DtoValidationPattern {
    private DtoValidationPattern() {
    }

    public static final String LETTERS_ONLY = "^[A-Za-zÀ-ü]+((\\s{1}[A-Za-zÀ-ü]+)+)?";
    public static final String ISBN_NUMBERS_ONLY = "^\\d{10,13}$";
    public static final String DATE_ONLY = "^\\d{4}-\\d{2}-\\d{2}$";
}

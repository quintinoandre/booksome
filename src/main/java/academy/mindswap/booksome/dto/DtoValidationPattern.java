package academy.mindswap.booksome.dto;

public final class DtoValidationPattern {
    private DtoValidationPattern() {
    }

    public static final String LETTERS_ONLY = "^[a-zA-Z]+((\\s{1}[a-zA-Z]+)+)?";
}

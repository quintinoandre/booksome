package academy.mindswap.booksome.dto;

public final class DtoValidationPattern {
    private DtoValidationPattern() {
    }

    public static final String NAME_PATTERN = "(([A-Za-zÀ-ü]+\\s?)|\\.\\s?)+";
    public static final String ISBN_PATTERN = "(\\d{10}|\\d{13})";
    public static final String PUBLISHED_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
}

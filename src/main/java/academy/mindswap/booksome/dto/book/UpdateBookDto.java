package academy.mindswap.booksome.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

import static academy.mindswap.booksome.dto.DtoValidationMessage.*;
import static academy.mindswap.booksome.dto.DtoValidationPattern.ISBN_PATTERN;
import static academy.mindswap.booksome.dto.DtoValidationPattern.PUBLISHED_DATE_PATTERN;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.INVALID_ISBN;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.INVALID_PUBLISHED_DATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto implements Serializable {
    @NotBlank(message = TITLE_MANDATORY)
    private String title;

    @NotNull(message = AUTHOR_MANDATORY)
    private List<String> authors;

    @NotNull(message = CATEGORY_MANDATORY)
    private List<String> category;

    @NotBlank(message = ISBN_MANDATORY)
    @Pattern(regexp = ISBN_PATTERN, message = INVALID_ISBN)
    private String isbn;

    @NotBlank(message = DESCRIPTION_MANDATORY)
    private String description;

    @NotBlank(message = PUBLISHED_DATE_MANDATORY)
    @Pattern(regexp = PUBLISHED_DATE_PATTERN, message = INVALID_PUBLISHED_DATE)
    private String publishedDate;

    @NotBlank(message = PUBLISHER_MANDATORY)
    private String publisher;
}

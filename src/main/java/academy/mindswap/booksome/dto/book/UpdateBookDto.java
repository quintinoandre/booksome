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
import static academy.mindswap.booksome.dto.DtoValidationPattern.*;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto implements Serializable {
    @NotBlank(message = TITLE_MANDATORY)
    @Pattern(regexp = LETTERS_ONLY, message = INVALID_TITLE)
    private String title;

    @NotNull(message = AUTHOR_MANDATORY)
    private List<String> authors;

    @NotNull(message = CATEGORY_MANDATORY)
    private List<String> category;

    @NotBlank(message = ISBN_MANDATORY)
    @Pattern(regexp = ISBN_NUMBERS_ONLY, message = INVALID_ISBN)
    private String isbn;

    @NotBlank(message = DESCRIPTION_MANDATORY)
    private String description;

    @NotBlank(message = PUBLISHED_DATE_MANDATORY)
    @Pattern(regexp = DATE_ONLY, message = INVALID_PUBLISHED_DATE)
    private String publishedDate;

    @NotBlank(message = PUBLISHER_MANDATORY)
    @Pattern(regexp = LETTERS_ONLY, message = INVALID_PUBLISHER)
    private String publisher;
}

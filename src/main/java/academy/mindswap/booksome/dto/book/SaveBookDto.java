package academy.mindswap.booksome.dto.book;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static academy.mindswap.booksome.dto.DtoValidationMessage.*;

@Data
@Builder
public class SaveBookDto {

    @NotBlank(message = TITLE_MANDATORY)
    private String title;
    @NotBlank(message = AUTHOR_MANDATORY)
    private List<String> authors;
    @NotBlank(message = CATEGORY_MANDATORY)
    private List<String> category;
    @NotBlank(message = ISBN_MANDATORY)
    private String isbn;
    @NotBlank(message = DESCRIPTION_MANDATORY)
    private String description;
    @NotBlank(message = PUBLISHED_DATE_MANDATORY)
    private String publishedDate;
    @NotBlank(message = PUBLISHER_MANDATORY)
    private String publisher;
}

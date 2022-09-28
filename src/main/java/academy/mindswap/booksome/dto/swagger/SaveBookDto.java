package academy.mindswap.booksome.dto.swagger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

import static academy.mindswap.booksome.dto.DtoValidationMessage.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBookDto implements Serializable {
    @NotBlank(message = TITLE_MANDATORY)
    private String title;

    @NotNull(message = AUTHOR_MANDATORY)
    private List<String> authors;

    @NotNull(message = CATEGORY_MANDATORY)
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

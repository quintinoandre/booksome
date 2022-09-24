package academy.mindswap.booksome.dto.book;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class SaveBookDto {

    private String title;
    private List<String> authors;
    private List<String> category;
    private String isbn;
    private String description;
    private String publishedDate;
    private String publisher;
}

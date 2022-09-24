package academy.mindswap.booksome.dto.book;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BookDto {

    private String id;
    private String title;
    private List<String> authors;
    private List<String> category;
    private String isbn;
    private String description;
    private String publishedDate;
    private String publisher;


}

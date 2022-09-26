package academy.mindswap.booksome.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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

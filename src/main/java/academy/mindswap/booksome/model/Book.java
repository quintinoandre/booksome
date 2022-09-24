package academy.mindswap.booksome.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Document(value = "books")
public class Book implements Serializable {

    @Id
    private String id;
    private String title;
    private List<String> authors;
    private List<String> category;
    private String isbn;
    private String description;
    private String publishedDate;
    private String publisher;


}

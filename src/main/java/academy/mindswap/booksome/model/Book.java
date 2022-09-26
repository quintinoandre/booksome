package academy.mindswap.booksome.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

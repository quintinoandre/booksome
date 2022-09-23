package academy.mindswap.booksome.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Document
public class Book implements Serializable {

    @Id
    private String id;
    private String title;
    private String[] authors;
    private String category;
    private Integer isbn;
    private String description;
    private Date publishedDate;
    private String publisher;


}

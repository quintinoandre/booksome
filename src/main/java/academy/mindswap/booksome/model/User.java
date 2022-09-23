package academy.mindswap.booksome.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@Document
public class User implements Serializable {

    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

}

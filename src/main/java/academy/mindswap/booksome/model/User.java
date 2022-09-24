package academy.mindswap.booksome.model;

import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Document(value = "users")
public class User implements Serializable {

    @Id
    private String id;
    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private List<String> favoriteBooksId;
    private List<String> readBooksId;

    private List<RoleTypes> roles;

}

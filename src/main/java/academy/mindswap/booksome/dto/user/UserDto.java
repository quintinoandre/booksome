package academy.mindswap.booksome.dto.user;

import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserDto {

    private String id;
    private String name;

    private String email;

    private List<String> favoriteBooksId;
    private List<String> readBooksId;

    private List<RoleTypes> roles;
}

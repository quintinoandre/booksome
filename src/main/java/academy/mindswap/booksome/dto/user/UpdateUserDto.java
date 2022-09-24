package academy.mindswap.booksome.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class UpdateUserDto {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 4)
    private String password;

    private List<String> favoriteBooksId;
    private List<String> readBooksId;
}

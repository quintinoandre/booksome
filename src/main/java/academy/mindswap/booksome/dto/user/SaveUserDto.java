package academy.mindswap.booksome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserDto {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 4)
    private String password;
}

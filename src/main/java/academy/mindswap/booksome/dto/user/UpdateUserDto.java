package academy.mindswap.booksome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static academy.mindswap.booksome.dto.DtoValidationMessage.EMAIL_MANDATORY;
import static academy.mindswap.booksome.dto.DtoValidationMessage.NAME_MANDATORY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto implements Serializable {
    @NotBlank(message = NAME_MANDATORY)
    private String name;

    @Email(message = EMAIL_MANDATORY)
    private String email;

    @Size(min = 4)
    private String password;
}

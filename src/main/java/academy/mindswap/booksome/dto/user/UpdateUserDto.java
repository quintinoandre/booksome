package academy.mindswap.booksome.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static academy.mindswap.booksome.dto.DtoValidationMessage.*;
import static academy.mindswap.booksome.dto.DtoValidationPattern.LETTERS_ONLY;
import static academy.mindswap.booksome.exception.user.UserExceptionMessage.INVALID_NAME;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto implements Serializable {
    @NotBlank(message = NAME_MANDATORY)
    @Pattern(regexp = LETTERS_ONLY, message = INVALID_NAME)
    private String name;

    @Email(message = EMAIL_MANDATORY)
    @NotBlank(message = EMAIL_NOT_BLANK)
    private String email;

    @Size(min = 4, message = PASSWORD_SIZE)
    @NotBlank(message = PASSWORD_MANDATORY)
    private String password;
}

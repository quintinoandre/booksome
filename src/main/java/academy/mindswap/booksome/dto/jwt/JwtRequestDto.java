package academy.mindswap.booksome.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

import static academy.mindswap.booksome.dto.DtoValidationMessage.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @Email(message = USERNAME_MANDATORY)
    @NotBlank(message = USERNAME_NOT_BLANK)
    private String username;

    @Size(min = 4, message = PASSWORD_SIZE)
    @NotBlank(message = PASSWORD_MANDATORY)
    private String password;
}

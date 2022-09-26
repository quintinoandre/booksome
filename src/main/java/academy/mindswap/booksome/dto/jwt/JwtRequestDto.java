package academy.mindswap.booksome.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

import static academy.mindswap.booksome.dto.DtoValidationMessage.PASSWORD_MANDATORY;
import static academy.mindswap.booksome.dto.DtoValidationMessage.USERNAME_MANDATORY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @NotBlank(message = USERNAME_MANDATORY)
    private String username;

    @NotBlank(message = PASSWORD_MANDATORY)
    private String password;
}

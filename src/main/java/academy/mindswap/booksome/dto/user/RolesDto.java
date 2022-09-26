package academy.mindswap.booksome.dto.user;

import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

import static academy.mindswap.booksome.dto.DtoValidationMessage.ROLE_MANDATORY;

@Data
public class RolesDto {
    @NotNull(message = ROLE_MANDATORY)
    private List<RoleTypes> roles;
}

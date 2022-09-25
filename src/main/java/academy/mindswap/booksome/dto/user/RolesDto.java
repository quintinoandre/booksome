package academy.mindswap.booksome.dto.user;

import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RolesDto {
    @NotNull(message = "Roles is mandatory")
    private List<RoleTypes> roles;
}

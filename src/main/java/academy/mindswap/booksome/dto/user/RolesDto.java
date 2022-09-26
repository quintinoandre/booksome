package academy.mindswap.booksome.dto.user;

import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RolesDto {
    @NotNull(message = "At least one role is mandatory")
    private List<RoleTypes> roles;
}

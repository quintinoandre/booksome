package academy.mindswap.booksome.dto.user;

import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

import static academy.mindswap.booksome.dto.DtoValidationMessage.ROLE_MANDATORY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto implements Serializable {
    @NotNull(message = ROLE_MANDATORY)
    private List<RoleTypes> roles;
}

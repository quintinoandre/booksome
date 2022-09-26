package academy.mindswap.booksome.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8091879091924046844L;

    private String token;
}
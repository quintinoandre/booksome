package academy.mindswap.booksome.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Getter
public class JwtResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8091879091924046844L;
    
    private final String token;
}
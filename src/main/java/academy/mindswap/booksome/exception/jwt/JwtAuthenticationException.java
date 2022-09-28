package academy.mindswap.booksome.exception.jwt;

import org.springframework.security.core.AuthenticationException;

public final class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}

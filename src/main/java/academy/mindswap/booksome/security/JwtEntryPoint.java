package academy.mindswap.booksome.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.UNAUTHORIZED;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Serial
    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);
    }
}
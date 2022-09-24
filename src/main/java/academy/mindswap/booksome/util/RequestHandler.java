package academy.mindswap.booksome.util;

import academy.mindswap.booksome.util.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class RequestHandler {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Autowired
    public RequestHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getUserId(HttpServletRequest request) {
        return jwtUtil.getUserIdFromToken(request.getHeader(AUTHORIZATION).substring(BEARER_PREFIX.length()));
    }
}

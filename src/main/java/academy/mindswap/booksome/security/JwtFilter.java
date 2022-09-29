package academy.mindswap.booksome.security;

import academy.mindswap.booksome.util.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.CANNOT_SET_SECURITY_CONTEXT;
import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.UNAUTHORIZED;
import static academy.mindswap.booksome.security.JwtFilterConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The JwtFilter class extends the Spring Web Filter OncePerRequestFilter class.
 * For any incoming request, this filter class is executed.
 * It checks that the request has a valid JWT token.
 * If it has a valid JWT token, it will set authentication in context to specify that the current user is authenticated.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${jwt.allowForRefreshTokenExpirationDateInMs}")
    private int allowForRefreshTokenExpirationDateInMs;

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * JWT Token is in "Bearer Token" format. So this method removes the word "Bearer" and gets just the Token.
     */
    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    private void allowForRefreshToken(ExpiredJwtException expiredJwtException, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(null, null, null);

        /*After setting Authentication in context, we specify that the current user is authenticated. So it passes
        Spring security settings successfully.*/
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        //Define the claims so that in the controller we use it to create a new JWT.
        request.setAttribute(CLAIMS, expiredJwtException.getClaims());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = extractJwtFromRequest(request);

        try {
            //If the token is valid, configure Spring Security to manually set authentication.
            if (StringUtils.hasText(jwtToken) && jwtUtil.validateToken(jwtToken)) {
                UserDetails userDetails = new User(jwtUtil.getUsernameFromToken(jwtToken), "",
                        jwtUtil.getRolesFromToken(jwtToken));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());

                /*After setting Authentication in context, we specify that the current user is authenticated. So it
                passes Spring security settings successfully.*/
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else if (!request.getRequestURL().toString().contains(AUTHENTICATE)) {
                LOGGER.error(CANNOT_SET_SECURITY_CONTEXT);
            }
        } catch (ExpiredJwtException exception) {
            String isRefreshToken = request.getHeader(IS_REFRESH_TOKEN);

            String requestURL = request.getRequestURL().toString();

            //Allow refresh token creation if the following conditions are true.
            if (isRefreshToken != null && isRefreshToken.equals(TRUE) && requestURL.contains(REFRESH_TOKEN) &&
                    !exception.getClaims().getExpiration().before(new Date(new Date().getTime() -
                            allowForRefreshTokenExpirationDateInMs))) {
                allowForRefreshToken(exception, request);
            } else {
                request.setAttribute(EXCEPTION, exception);

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);

                return;
            }
        } catch (
                BadCredentialsException exception) {
            request.setAttribute(EXCEPTION, exception);
        } catch (
                Exception exception) {
            LOGGER.error(exception.toString());
        }

        filterChain.doFilter(request, response);
    }
}

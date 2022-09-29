package academy.mindswap.booksome.util.jwt;

import academy.mindswap.booksome.service.interfaces.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.INVALID_CREDENTIALS;
import static academy.mindswap.booksome.util.jwt.JwtUtilConstant.ROLES;
import static academy.mindswap.booksome.util.jwt.JwtUtilConstant.USER_ID;

/**
 * This class is responsible for performing JWT operations such as creation and validation. It makes use of
 * io.jsonwebtoken.Jwts to achieve this.
 */
@Component
public class JwtUtil implements Serializable {
    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs;

    @Value("${jwt.refreshExpirationDateInMs}")
    private int refreshExpirationDateInMs;

    private final UserService userService;

    @Autowired
    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method retrieves any information from the token, but for that we need the secret key.
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * This method retrieves a chosen information from the token.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    /**
     * This method retrieves the "username" of the user from the jwt token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * When creating the token this method:
     * 1. Set the token claims such as Issuer, Expiration, Subject and ID
     * 2. Sign the JWT using the HS256 algorithm and the secret key
     * 3. According to JWS Compact Serialization
     * (https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1) JWT compression for a URL-safe
     * string
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * This method generates the token for the user and adds the user's roles and user Id to the claims.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(ROLES, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        claims.put(USER_ID, userService.findByEmail(userDetails.getUsername()).getId());

        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * This method validates the token and for that we need the secret key.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException exception) {
            throw new BadCredentialsException(INVALID_CREDENTIALS, exception);
        } catch (ExpiredJwtException exception) {
            throw exception;
        }
    }

    /**
     * This method retrieves user roles from his token.
     */
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        List<String> roles = (List<String>) claims.get(ROLES);

        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    /**
     * This method retrieves user Id from his token.
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        return (String) claims.get(USER_ID);
    }
}

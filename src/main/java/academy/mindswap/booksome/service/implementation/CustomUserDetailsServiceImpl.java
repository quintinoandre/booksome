package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.exception.jwt.JwtAuthenticationException;
import academy.mindswap.booksome.exception.user.UserNotFoundException;
import academy.mindswap.booksome.model.User;
import academy.mindswap.booksome.service.interfaces.CustomUserDetailsService;
import academy.mindswap.booksome.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.INVALID_CREDENTIALS;

/**
 * This class implements Spring Security's UserDetailsService interface and overrides the loadUserByUsername method,
 * to fetch the user's details from the database using the username.
 * Spring Security Authentication Manager calls this method to get the user details from the database when
 * authenticating the user details provided by the user.
 * In addition, a user's password is stored in an encrypted format using BCrypt.
 */
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private static final String ROLE_PREFIX = "ROLE_";

    private final UserService userService;

    @Autowired
    public CustomUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userService.findByEmail(email);

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    new ArrayList<>(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(ROLE_PREFIX
                            .concat(role.toString()))).toList()));
        } catch (UserNotFoundException exception) {
            throw new JwtAuthenticationException(INVALID_CREDENTIALS);
        }
    }
}

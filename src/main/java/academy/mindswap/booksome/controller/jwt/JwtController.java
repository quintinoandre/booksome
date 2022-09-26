package academy.mindswap.booksome.controller.jwt;

import academy.mindswap.booksome.dto.jwt.JwtRequestDto;
import academy.mindswap.booksome.dto.jwt.JwtResponseDto;
import academy.mindswap.booksome.exception.jwt.JwtBadRequestException;
import academy.mindswap.booksome.service.interfaces.CustomUserDetailsService;
import academy.mindswap.booksome.util.jwt.JwtUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static academy.mindswap.booksome.controller.jwt.JwtControllerConstant.CLAIMS;
import static academy.mindswap.booksome.controller.jwt.JwtControllerConstant.SUB;
import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.*;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class JwtController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                         CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException exception) {
            throw new Exception(USER_DISABLED, exception);
        } catch (BadCredentialsException exception) {
            throw new Exception(INVALID_CREDENTIALS, exception);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createToken(@Valid @RequestBody JwtRequestDto jwtRequestDto,
                                         BindingResult bindingResult) throws Exception {
        if (jwtRequestDto == null) {
            throw new JwtBadRequestException(AUTHENTICATION_NULL);
        }

        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        authenticate(jwtRequestDto.getUsername(), jwtRequestDto.getPassword());

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequestDto.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.OK);
    }

    @GetMapping("/refreshtoken")
    public ResponseEntity<JwtResponseDto> createRefreshToken(String headerStr, HttpServletRequest request) {
        if (request == null) {
            throw new JwtBadRequestException(REQUEST_NULL);
        }

        DefaultClaims claims = (DefaultClaims) request.getAttribute(CLAIMS);

        Map<String, Object> expectedMap = new HashMap<>(claims);

        String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get(SUB).toString());

        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.OK);
    }
}

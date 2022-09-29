package academy.mindswap.booksome.controller.jwt;

import academy.mindswap.booksome.dto.jwt.JwtRequestDto;
import academy.mindswap.booksome.dto.jwt.JwtResponseDto;
import academy.mindswap.booksome.exception.ExceptionError;
import academy.mindswap.booksome.exception.jwt.JwtAuthenticationException;
import academy.mindswap.booksome.exception.jwt.JwtBadRequestException;
import academy.mindswap.booksome.service.interfaces.CustomUserDetailsService;
import academy.mindswap.booksome.util.jwt.JwtUtil;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static academy.mindswap.booksome.controller.jwt.JwtControllerConstant.*;
import static academy.mindswap.booksome.exception.jwt.JwtExceptionMessage.*;

@Tag(name = "Authentication", description = "Contains all the operations to generate a JWT token and a JWT refresh " +
        "token.")
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

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException exception) {
            throw new JwtAuthenticationException(USER_DISABLED);
        } catch (BadCredentialsException exception) {
            throw new JwtAuthenticationException(INVALID_CREDENTIALS);
        }
    }

    /**
     * This method gets the username and password from the request body and using Spring Authentication Manager we
     * authenticate the username and password.
     * If the credentials are valid, the JWT token will be created using the JwtUtil class and provided to the user.
     */
    @Operation(summary = "Generate JWT token", description = "Generate a JWT token.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = JwtResponseDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @PostMapping("/authenticate")
    public ResponseEntity<?> createToken(@Valid @RequestBody JwtRequestDto jwtRequestDto,
                                         BindingResult bindingResult) {
        if (jwtRequestDto == null) {
            throw new JwtAuthenticationException(INVALID_CREDENTIALS);
        }

        if (bindingResult.hasErrors()) {
            throw new JwtAuthenticationException(INVALID_CREDENTIALS);
        }

        authenticate(jwtRequestDto.getUsername(), jwtRequestDto.getPassword());

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequestDto.getUsername());

        final String token = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.OK);
    }

    /**
     * This method gets the claims from the request/token and using the JwtUtil class creates a refresh token.
     */
    @Operation(summary = "Generate JWT refresh token", description = "Generate a JWT refresh token.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = JwtResponseDto.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/refreshtoken")
    public ResponseEntity<JwtResponseDto> createRefreshToken(@RequestHeader(value = IS_REFRESH_TOKEN,
            defaultValue = "true") String headerStr, HttpServletRequest request) {
        if (request == null) {
            throw new JwtBadRequestException(REQUEST_NULL);
        }

        DefaultClaims claims = (DefaultClaims) request.getAttribute(CLAIMS);

        Map<String, Object> expectedMap = new HashMap<>(claims);

        String token = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get(SUB).toString());

        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.OK);
    }
}

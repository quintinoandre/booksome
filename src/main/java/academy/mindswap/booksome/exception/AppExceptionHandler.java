package academy.mindswap.booksome.exception;

import academy.mindswap.booksome.exception.client.Client4xxErrorException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = {NotFoundException.class, BadRequestException.class, AuthenticationException.class,
            AccessDeniedException.class, Client4xxErrorException.class, Client4xxErrorException.class})
    public ResponseEntity<ExceptionError> handleException(Exception exception, HttpServletRequest request) {
        String logeErrorMessage = request.getMethod()
                .concat(" ")
                .concat(request.getRequestURI())
                .concat(": ")
                .concat(exception.getMessage());

        LOGGER.error(logeErrorMessage);

        return new ResponseEntity<>(ExceptionError.builder()
                .timestamp(new Date())
                .message(exception.getMessage())
                .method(request.getMethod())
                .path(request.getRequestURI())
                .build(), HttpStatus.NOT_FOUND);
    }
}

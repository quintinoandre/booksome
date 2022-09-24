package academy.mindswap.booksome.exception.jwt;

import academy.mindswap.booksome.exception.BadRequestException;

public class JwtBadRequestException extends BadRequestException {
    public JwtBadRequestException(String message) {
        super(message);
    }
}

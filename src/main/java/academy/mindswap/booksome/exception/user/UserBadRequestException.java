package academy.mindswap.booksome.exception.user;

import academy.mindswap.booksome.exception.BadRequestException;

public final class UserBadRequestException extends BadRequestException {
    public UserBadRequestException(String message) {
        super(message);
    }
}
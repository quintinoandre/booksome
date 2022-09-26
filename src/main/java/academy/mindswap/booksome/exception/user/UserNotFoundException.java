package academy.mindswap.booksome.exception.user;

import academy.mindswap.booksome.exception.NotFoundException;

import static academy.mindswap.booksome.exception.user.UserExceptionMessage.USER_NOT_FOUND;

public final class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}

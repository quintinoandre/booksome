package academy.mindswap.booksome.exception.user;

import academy.mindswap.booksome.exception.NotFoundException;

import static academy.mindswap.booksome.exception.user.UserExceptionMessage.USERS_NOT_FOUND;

public final class UsersNotFoundException extends NotFoundException {
    public UsersNotFoundException() {
        super(USERS_NOT_FOUND);
    }
}

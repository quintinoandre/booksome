package academy.mindswap.booksome.exception.book;

import academy.mindswap.booksome.exception.BadRequestException;

public final class BookBadRequestException extends BadRequestException {

    public BookBadRequestException(String message) {
        super(message);
    }
}

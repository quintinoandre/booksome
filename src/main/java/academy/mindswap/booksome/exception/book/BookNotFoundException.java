package academy.mindswap.booksome.exception.book;

import academy.mindswap.booksome.exception.NotFoundException;

public final class BookNotFoundException extends NotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}

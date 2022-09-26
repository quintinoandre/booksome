package academy.mindswap.booksome.exception.book;

import academy.mindswap.booksome.exception.NotFoundException;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.BOOK_NOT_FOUND;

public final class BookNotFoundException extends NotFoundException {
    public BookNotFoundException() {
        super(BOOK_NOT_FOUND);
    }
}

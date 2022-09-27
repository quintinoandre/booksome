package academy.mindswap.booksome.exception.book;

import academy.mindswap.booksome.exception.NotFoundException;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.BOOKS_NOT_FOUND;

public final class BooksNotFoundException extends NotFoundException {
    public BooksNotFoundException() {
        super(BOOKS_NOT_FOUND);
    }
}

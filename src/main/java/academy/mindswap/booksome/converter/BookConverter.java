package academy.mindswap.booksome.converter;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.model.Book;
import org.modelmapper.ModelMapper;

public final class BookConverter {
    private BookConverter() {
    }

    public static BookDto convertBookToBookDto(Book book) {
        return new ModelMapper().map(book, BookDto.class);
    }

    public static Book convertBookClientDtoToBook(BookClientDto bookClientDto) {
        return new ModelMapper().map(bookClientDto, Book.class);
    }
}

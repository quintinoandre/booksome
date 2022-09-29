package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.UpdateBookDto;
import academy.mindswap.booksome.model.Book;

import java.util.List;
import java.util.Map;

/**
 * Interface that has all methods to be implemented on "BookServic Impl" Class
 */
public interface BookService {
    BookDto save(BookClientDto bookClientDto);

    BookDto findById(String id);

    List<BookDto> findAll();

    List<?> searchAll(Map<String, String> allParams);

    Book findByIsbn(String isbn);

    void verifyBookExists(String id);

    BookDto update(String id, UpdateBookDto updateBookDto);

    void delete(String id);

    Book findBook(String id);
}

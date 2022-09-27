package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.model.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    BookDto save(BookClientDto bookClientDto);

    List<BookDto> findAll();

    List<?> searchAll(Map<String, String> allParams);

    Book findByIsbn(String isbn);

    void verifyBookExists(String id);

    void delete(String id);
}

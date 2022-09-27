package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.model.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    BookDto save(BookClientDto bookClientDto);

    List<?> findAll(Map<String, String> allParams);

    Book findByIsbn(String isbn);

    void verifyBookKExists(String id);
}

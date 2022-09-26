package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.SaveBookDto;
import academy.mindswap.booksome.model.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    BookDto save(BookClientDto bookClientDto);

    public List<?> findAll(Map<String, String> allParams);
    public Book findByIsbn(String isbn);
}

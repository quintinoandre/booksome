package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.book.BookDto;

import java.util.List;
import java.util.Map;

public interface BookService {
    public List<BookDto> findAll(Map<String, String> allParams);
}

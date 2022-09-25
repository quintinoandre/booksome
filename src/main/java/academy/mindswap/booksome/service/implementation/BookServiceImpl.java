package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.client.GoogleBooksClient;
import academy.mindswap.booksome.converter.BookConverter;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.repository.BookRepository;
import academy.mindswap.booksome.service.interfaces.BookService;
import academy.mindswap.booksome.util.book.BookSearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static academy.mindswap.booksome.service.implementation.ServiceConstant.*;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GoogleBooksClient googleBooksClient;
    
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, GoogleBooksClient googleBooksClient) {
        this.bookRepository = bookRepository;
        this.googleBooksClient = googleBooksClient;
    }

    public List<?> findAll(Map<String, String> allParams) {
        Map<String, String> filteredBookSearch = BookSearchFilter.filterSearch(allParams);
        String title = filteredBookSearch.get(TITLE);
        String authors = filteredBookSearch.get(AUTHORS);
        String category = filteredBookSearch.get(CATEGORY);
        String isbn = filteredBookSearch.get(ISBN);
        List<BookDto> bookDto = bookRepository.findAll(title, authors, category, isbn)
                .stream()
                .map(BookConverter::convertBookToBookDto).toList();
        return bookDto.isEmpty() ? googleBooksClient.findAll(title, authors, category, isbn) : bookDto;
    }
}

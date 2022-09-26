package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.client.GoogleBooksClient;
import academy.mindswap.booksome.converter.BookConverter;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.exception.client.Client4xxErrorException;
import academy.mindswap.booksome.exception.client.Client5xxErrorException;
import academy.mindswap.booksome.repository.BookRepository;
import academy.mindswap.booksome.service.interfaces.BookService;
import academy.mindswap.booksome.util.book.BookSearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static academy.mindswap.booksome.service.implementation.ServiceConstant.*;

import java.util.ArrayList;
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

    private List<BookDto> findInDatabase(String title, String authors, String category, String isbn) {
        return bookRepository.findAll(title, authors, category, isbn)
                .stream()
                .map(BookConverter::convertBookToBookDto).toList();
    }

    public List<?> findAll(Map<String, String> allParams) {
        Map<String, String> filteredBookSearch = BookSearchFilter.filterSearch(allParams);
        String title = filteredBookSearch.get(TITLE);
        String authors = filteredBookSearch.get(AUTHORS);
        String category = filteredBookSearch.get(CATEGORY);
        String isbn = filteredBookSearch.get(ISBN);

        List<?> bookDto = new ArrayList<>();

        if (isbn != null) {
            bookDto = findInDatabase(title, authors, category, isbn);

            if (bookDto.isEmpty()) {
                try {
                    bookDto = googleBooksClient.findAll(title, authors, category, isbn);
                } catch (RuntimeException exception) {
                    return bookDto;
                }
            }
            return bookDto;
        }

        try {
            bookDto = googleBooksClient.findAll(title, authors, category, isbn);
        } catch (RuntimeException exception) {
            bookDto = findInDatabase(title, authors, category, isbn);
        }
        return bookDto;
    }
}

package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.client.GoogleBooksClient;
import academy.mindswap.booksome.converter.BookConverter;
import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.SaveBookDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.exception.book.BookNotFoundException;
import academy.mindswap.booksome.exception.client.Client4xxErrorException;
import academy.mindswap.booksome.exception.client.Client5xxErrorException;
import academy.mindswap.booksome.model.Book;
import academy.mindswap.booksome.repository.BookRepository;
import academy.mindswap.booksome.service.interfaces.BookService;
import academy.mindswap.booksome.util.book.BookSearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.ISBN_ALREADY_EXISTS;
import static academy.mindswap.booksome.service.implementation.ServiceConstant.*;
import static academy.mindswap.booksome.util.book.BookMessage.BOOK_SAVED;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GoogleBooksClient googleBooksClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

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

    private void verifyIsbnExists(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new BookBadRequestException(ISBN_ALREADY_EXISTS);
        }
    }

    @Override
    public BookDto save(BookClientDto bookClientDto) {
        Book bookEntity = BookConverter.convertBookClientDtoToBook(bookClientDto);
        verifyIsbnExists(bookClientDto.getIsbn());
        LOGGER.info(BOOK_SAVED);
        return BookConverter.convertBookToBookDto(bookRepository.insert(bookEntity));
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

    @Override
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}

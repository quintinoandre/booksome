package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.client.GoogleBooksClient;
import academy.mindswap.booksome.converter.BookConverter;
import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.exception.book.BookNotFoundException;
import academy.mindswap.booksome.exception.book.BooksNotFoundException;
import academy.mindswap.booksome.model.Book;
import academy.mindswap.booksome.repository.BookRepository;
import academy.mindswap.booksome.service.interfaces.BookService;
import academy.mindswap.booksome.util.book.BookSearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.ISBN_ALREADY_EXISTS;
import static academy.mindswap.booksome.service.implementation.BookServiceConstant.*;
import static academy.mindswap.booksome.util.book.BookMessage.*;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final GoogleBooksClient googleBooksClient;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, GoogleBooksClient googleBooksClient) {
        this.bookRepository = bookRepository;
        this.googleBooksClient = googleBooksClient;
    }

    private List<BookDto> findInDatabase(String title, String authors, String category, String isbn) {
        return bookRepository.searchAll(title, authors, category, isbn)
                .stream()
                .map(BookConverter::convertBookToBookDto).toList();
    }

    private void verifyIsbnExists(String isbn) {
        if (Boolean.TRUE.equals(bookRepository.existsByIsbn(isbn))) {
            throw new BookBadRequestException(ISBN_ALREADY_EXISTS);
        }
    }

    @Override
    @CacheEvict(value = "books", allEntries = true)
    public BookDto save(BookClientDto bookClientDto) {
        Book bookEntity = BookConverter.convertBookClientDtoToBook(bookClientDto);

        verifyIsbnExists(bookClientDto.getIsbn());

        LOGGER.info(BOOK_SAVED);

        return BookConverter.convertBookToBookDto(bookRepository.insert(bookEntity));
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookDto findById(String id) {
        return BookConverter.convertBookToBookDto(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }

    @Override
    @Cacheable(value = "books")
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            throw new BooksNotFoundException();
        }

        LOGGER.info(BOOKS_FOUND);

        return books.stream().map(BookConverter::convertBookToBookDto).toList();
    }

    @Override
    public List<?> searchAll(Map<String, String> allParams) {
        Map<String, String> filteredBookSearch = BookSearchFilter.filterSearch(allParams);

        String title = filteredBookSearch.get(TITLE);
        String authors = filteredBookSearch.get(AUTHORS);
        String category = filteredBookSearch.get(CATEGORY);
        String isbn = filteredBookSearch.get(ISBN);

        List<?> bookDto;

        if (isbn != null) {
            bookDto = findInDatabase(title, authors, category, isbn);

            if (bookDto.isEmpty()) {
                try {
                    bookDto = googleBooksClient.searchAll(title, authors, category, isbn);

                    if (bookDto.isEmpty()) {
                        throw new BookNotFoundException();
                    }
                } catch (RuntimeException exception) {
                    throw new BookNotFoundException();
                }
            }

            LOGGER.info(BOOKS_FOUND);

            return bookDto;
        }

        try {
            bookDto = googleBooksClient.searchAll(title, authors, category, isbn);
        } catch (RuntimeException exception) {
            bookDto = findInDatabase(title, authors, category, isbn);
        }

        LOGGER.info(BOOKS_FOUND);

        return bookDto;
    }

    @Override
    @Cacheable(value = "books", key = "#isbn")
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public void verifyBookExists(String id) {
        if (bookRepository.existsById(id)) {
            return;
        }

        throw new BookNotFoundException();
    }

    @Override
    @CacheEvict(value = "books", allEntries = true)
    public void delete(String id) {
        verifyBookExists(id);

        LOGGER.info(BOOK_DELETED);

        bookRepository.deleteById(id);
    }
}

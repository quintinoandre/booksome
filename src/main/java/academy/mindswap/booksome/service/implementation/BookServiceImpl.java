package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.client.GoogleBooksClient;
import academy.mindswap.booksome.converter.BookConverter;
import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.UpdateBookDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.exception.book.BookNotFoundException;
import academy.mindswap.booksome.exception.book.BooksNotFoundException;
import academy.mindswap.booksome.model.Book;
import academy.mindswap.booksome.repository.BookRepository;
import academy.mindswap.booksome.service.interfaces.BookService;
import academy.mindswap.booksome.service.interfaces.UserService;
import academy.mindswap.booksome.util.book.BookSearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.BOOK_EXISTS_IN_USER;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.ISBN_ALREADY_EXISTS;
import static academy.mindswap.booksome.service.implementation.BookServiceConstant.*;
import static academy.mindswap.booksome.util.book.BookMessage.*;

/**
 * This class has all logic regarding books connection with database and api
 */
@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final GoogleBooksClient googleBooksClient;
    private final UserService userService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, GoogleBooksClient googleBooksClient, @Lazy UserService userService) {
        this.bookRepository = bookRepository;
        this.googleBooksClient = googleBooksClient;
        this.userService = userService;
    }

    /**
     * Search for books in database with the given params, all params are optional to be passed
     *
     * @param title
     * @param authors
     * @param category
     * @param isbn
     * @return a list of BooksDto with the params passed, or an empty list
     */
    private List<BookDto> findInDatabase(String title, String authors, String category, String isbn) {
        return bookRepository.searchAll(title, authors, category, isbn)
                .stream()
                .map(BookConverter::convertBookToBookDto).toList();
    }

    /**
     * Checks if a book exists by the given ISBN. if not throws exception
     *
     * @param isbn
     */
    private void verifyIsbnExists(String isbn) {
        if (Boolean.TRUE.equals(bookRepository.existsByIsbn(isbn))) {
            throw new BookBadRequestException(ISBN_ALREADY_EXISTS);
        }
    }

    /**
     * Inserts a book in the database if not existing already
     *
     * @param bookClientDto
     * @return the Book inserted converted to Dto
     */
    @Override
    @CacheEvict(value = "books", allEntries = true)
    public BookDto save(BookClientDto bookClientDto) {
        Book bookEntity = BookConverter.convertBookClientDtoToBook(bookClientDto);

        verifyIsbnExists(bookClientDto.getIsbn());

        LOGGER.info(BOOK_SAVED);

        return BookConverter.convertBookToBookDto(bookRepository.insert(bookEntity));
    }

    /**
     * Find book by specific id
     *
     * @param id
     * @return the book found
     */
    @Override
    @Cacheable(value = "books", key = "#id")
    public BookDto findById(String id) {
        Book book = findBook(id);

        LOGGER.info(BOOK_FOUND);

        return BookConverter.convertBookToBookDto(book);
    }

    /**
     * Search for all books
     *
     * @return List of all books converted to Dto, or empty
     */
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

    /******
     * This method will first accept as arguments the params passed on the Uri search. Then will extract those params into variables and check if ISBN was passed
     * to perform a search in database or external  Api.
     * If not Isbn then performs a search on the external Api
     * If can't find throw exception
     *
     * @param allParams
     * @return list of bookDto
     */
    @Override
    public List<?> searchAll(Map<String, String> allParams) {
        Map<String, String> filteredBookSearch = BookSearchFilter.filterSearch(allParams);

        String title = filteredBookSearch.get(TITLE);
        String authors = filteredBookSearch.get(AUTHORS);
        String category = filteredBookSearch.get(CATEGORY);
        String isbn = filteredBookSearch.get(ISBN);

        List<?> bookDtoList;

        if (isbn != null) {
            bookDtoList = findInDatabase(title, authors, category, isbn);

            if (bookDtoList.isEmpty()) {
                try {
                    bookDtoList = googleBooksClient.searchAll(title, authors, category, isbn);

                    if (bookDtoList.isEmpty()) {
                        throw new BooksNotFoundException();
                    }
                } catch (RuntimeException exception) {
                    throw new BooksNotFoundException();
                }
            }

            LOGGER.info(BOOKS_FOUND);

            return bookDtoList;
        }

        try {
            bookDtoList = findInDatabase(title, authors, category, isbn);
            if (bookDtoList.isEmpty()) {
                throw new BooksNotFoundException();
            }
        } catch (RuntimeException exception) {
            bookDtoList = googleBooksClient.searchAll(title, authors, category, isbn);

            if (bookDtoList.isEmpty()) {
                throw new BooksNotFoundException();
            }
        }

        LOGGER.info(BOOKS_FOUND);

        return bookDtoList;
    }

    /**
     * Search on database by book with given isbn
     *
     * @param isbn
     * @return Book
     */
    @Override
    @Cacheable(value = "books", key = "#isbn")
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    /**
     * Checks if books exists by given id or throw exception
     *
     * @param id
     */
    @Override
    public void verifyBookExists(String id) {
        if (bookRepository.existsById(id)) {
            return;
        }

        throw new BookNotFoundException();
    }

    /*******
     * Verifies if the book passed by has the same params from the book to be updated
     * If params are different, new params are set to the book in database and saved
     *
     * @param id
     * @param updateBookDto
     * @return BookDto of the updated book
     */
    @Override
    @CacheEvict(value = "books", allEntries = true)
    public BookDto update(String id, UpdateBookDto updateBookDto) {
        Book bookEntity = BookConverter.convertUpdateBookDtoToBook(updateBookDto);

        Book updatedBook = findBook(id);

        if (bookEntity.getTitle() != null && !bookEntity.getTitle().equals(updatedBook.getTitle())) {
            updatedBook.setTitle(bookEntity.getTitle());
        }
        if (bookEntity.getAuthors() != null) {
            updatedBook.setAuthors(bookEntity.getAuthors());
        }
        if (bookEntity.getCategory() != null) {
            updatedBook.setCategory(bookEntity.getCategory());
        }
        if (bookEntity.getIsbn() != null && !bookEntity.getIsbn().equals(updatedBook.getIsbn())) {
            updatedBook.setIsbn(bookEntity.getIsbn());
        }
        if (bookEntity.getDescription() != null && !bookEntity.getDescription().equals(updatedBook.getDescription())) {
            updatedBook.setDescription(bookEntity.getDescription());
        }
        if (bookEntity.getPublishedDate() != null && !bookEntity.getPublishedDate().equals(updatedBook
                .getPublishedDate())) {
            updatedBook.setPublishedDate(bookEntity.getPublishedDate());
        }
        if (bookEntity.getPublisher() != null && !bookEntity.getPublisher().equals(updatedBook
                .getPublisher())) {
            updatedBook.setPublisher(bookEntity.getPublisher());
        }

        LOGGER.info(BOOK_UPDATED);

        return BookConverter.convertBookToBookDto(bookRepository.save(updatedBook));
    }

    /**
     * Verifies if the book to be deleted exist in database or throws exception
     * Then checks if books is saved in any list of favorites or read books of a user
     * If saved, throws an exception and cannot be deleted
     * If not existing in any users list, it can be deleted
     *
     * @param id
     */
    @Override
    @CacheEvict(value = "books", allEntries = true)
    public void delete(String id) {
        verifyBookExists(id);

        if (Boolean.TRUE.equals(userService.existsByFavoriteBooksId(id)) ||
                Boolean.TRUE.equals(userService.existsByReadBooksId(id))) {
            throw new BookBadRequestException(BOOK_EXISTS_IN_USER);
        }

        LOGGER.info(BOOK_DELETED);

        bookRepository.deleteById(id);
    }

    /**
     * Search book by given id
     *
     * @param id
     * @return Book or throws exception
     */
    @Override
    @Cacheable(value = "books", key = "#id")
    public Book findBook(String id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }
}

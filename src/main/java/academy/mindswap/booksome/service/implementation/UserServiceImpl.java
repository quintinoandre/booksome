package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.client.GoogleBooksClient;
import academy.mindswap.booksome.converter.BookConverter;
import academy.mindswap.booksome.converter.UserConverter;
import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.user.RolesDto;
import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.exception.book.BookNotFoundException;
import academy.mindswap.booksome.exception.book.BooksNotFoundException;
import academy.mindswap.booksome.exception.user.UserBadRequestException;
import academy.mindswap.booksome.exception.user.UserNotFoundException;
import academy.mindswap.booksome.exception.user.UsersNotFoundException;
import academy.mindswap.booksome.model.Book;
import academy.mindswap.booksome.model.User;
import academy.mindswap.booksome.repository.UserRepository;
import academy.mindswap.booksome.service.interfaces.BookService;
import academy.mindswap.booksome.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static academy.mindswap.booksome.exception.user.UserExceptionMessage.*;
import static academy.mindswap.booksome.util.user.UserMessage.*;

/**
 * This class has all the logic regarding the user management
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;
    private final BookService bookService;
    private final GoogleBooksClient googleBooksClient;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder bcryptEncoder, BookService bookService, GoogleBooksClient googleBooksClient) {
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
        this.bookService = bookService;
        this.googleBooksClient = googleBooksClient;
    }

    private void verifyEmailExits(String email) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new UserBadRequestException(EMAIL_ALREADY_EXISTS);
        }
    }

    /*******
     * Searches for a given isbn book on database or Api to set on user favorite list
     * Checks if book is already on favorites list
     * @param isbn
     * @param userId
     * @return A book Dto inserted on user's favorites list
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto saveBookAsFavorite(String isbn, String userId) {
        BookDto bookDto;

        Book book = bookService.findByIsbn(isbn);

        if (book == null) {
            List<BookClientDto> bookClientDto = googleBooksClient.searchAll("", "", "", isbn);

            bookDto = bookService.save(bookClientDto.stream().findAny().orElseThrow(BookNotFoundException::new));
        } else {
            bookDto = BookConverter.convertBookToBookDto(book);
        }

        User user = findUser(userId);

        List<String> favoriteBooksId = new LinkedList<>();

        if (user.getFavoriteBooksId() != null) {
            favoriteBooksId.addAll(user.getFavoriteBooksId());

            if (user.getFavoriteBooksId().contains(bookDto.getId())) {
                throw new UserBadRequestException(ALREADY_FAVORITE);
            }
        }

        favoriteBooksId.add(bookDto.getId());

        user.setFavoriteBooksId(favoriteBooksId);

        LOGGER.info(ADDED_FAVORITE_BOOK);

        return UserConverter.convertUserToUserDto(userRepository.save(user));
    }

    /*******
     * Searches for a given isbn book on database or Api to set on user read list
     * Checks if book is already on read list
     * @param isbn
     * @param userId
     * @return A book Dto inserted on user's read list
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto saveBookAsRead(String isbn, String userId) {
        BookDto bookDto;

        Book book = bookService.findByIsbn(isbn);

        if (book == null) {
            List<BookClientDto> bookClientDto = googleBooksClient.searchAll("", "", "", isbn);

            bookDto = bookService.save(bookClientDto.stream().findAny().orElseThrow(BookNotFoundException::new));
        } else {
            bookDto = BookConverter.convertBookToBookDto(book);
        }

        User user = findUser(userId);

        List<String> readBooksId = new LinkedList<>();

        if (user.getReadBooksId() != null) {
            readBooksId.addAll(user.getReadBooksId());

            if (user.getReadBooksId().contains(bookDto.getId())) {
                throw new UserBadRequestException(ALREADY_READ);
            }
        }

        readBooksId.add(bookDto.getId());

        user.setReadBooksId(readBooksId);

        LOGGER.info(ADDED_READ_BOOK);

        return UserConverter.convertUserToUserDto(userRepository.save(user));
    }

    /**
     * Inserts users in database with a check if email exists already
     *
     * @param saveUserDto
     * @return User Dto inserted
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto save(SaveUserDto saveUserDto) {
        User userEntity = UserConverter.convertSaveUserDtoToUser(saveUserDto);

        verifyEmailExits(userEntity.getEmail());

        userEntity.setPassword(bcryptEncoder.encode(userEntity.getPassword()));

        LOGGER.info(USER_SAVED);

        return UserConverter.convertUserToUserDto(userRepository.insert(userEntity));
    }

    /**
     * Find all users in database
     * Throws exception if no users found
     *
     * @return
     */
    @Override
    @Cacheable(value = "users")
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UsersNotFoundException();
        }

        LOGGER.info(USERS_FOUND);

        return users.stream().map(UserConverter::convertUserToUserDto).toList();
    }

    /****
     * Find a favorite book by a given id
     * @param id book id
     * @param userId the user ID
     * @returnthe book searched
     */
    @Override
    @Cacheable(value = "users", key = "#id.concat('-favoriteBook')")
    public BookDto findFavoriteBook(String id, String userId) {
        List<String> favoriteBooksIds = findUser(userId).getFavoriteBooksId();

        if (favoriteBooksIds == null || favoriteBooksIds.isEmpty()) {
            throw new BookNotFoundException();
        }

        return bookService.findById(favoriteBooksIds
                .stream()
                .filter(bookId -> bookId.equals(id))
                .findAny()
                .orElseThrow(BookNotFoundException::new));
    }

    /****
     * Find a read book by a given id
     * @param id book id
     * @param userId the user ID
     * @returnthe book searched
     */
    @Override
    @Cacheable(value = "users", key = "#id.concat('-readBook')")
    public BookDto findReadBook(String id, String userId) {
        List<String> readBooksIds = findUser(userId).getReadBooksId();

        if (readBooksIds == null || readBooksIds.isEmpty()) {
            throw new BookNotFoundException();
        }

        return bookService.findById(readBooksIds
                .stream()
                .filter(bookId -> bookId.equals(id))
                .findAny()
                .orElseThrow(BookNotFoundException::new));
    }

    /*****
     * Find the list of all favorites books of a given user Id
     * Throws exception if empty
     * @param id
     * @return the list of books
     */
    @Override
    @Cacheable(value = "users", key = "#id.concat(':favoriteBooks')")
    public List<BookDto> findFavoriteBooks(String id) {
        List<String> favoriteBooksIds = findUser(id).getFavoriteBooksId();

        if (favoriteBooksIds == null || favoriteBooksIds.isEmpty()) {
            throw new BooksNotFoundException();
        }

        return favoriteBooksIds
                .stream()
                .map(bookService::findById)
                .toList();
    }

    /*****
     * Find the list of all read books of a given user Id
     * Throws exception if empty
     * @param id
     * @return the list of books
     */
    @Override
    @Cacheable(value = "users", key = "#id.concat(':readBooks')")
    public List<BookDto> findReadBooks(String id) {
        List<String> readBooksIds = findUser(id).getReadBooksId();

        if (readBooksIds == null || readBooksIds.isEmpty()) {
            throw new BooksNotFoundException();
        }

        return readBooksIds
                .stream()
                .map(bookService::findById)
                .toList();
    }

    /**
     * Find user by given id
     *
     * @param id
     * @return user Dto
     */
    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDto findById(String id) {
        User user = findUser(id);

        LOGGER.info(USER_FOUND);

        return UserConverter.convertUserToUserDto(user);
    }

    /**
     * Sets a given role to the user ( Admin , User  )
     *
     * @param id
     * @param rolesDto
     * @return the updated user
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto assignRoles(String id, RolesDto rolesDto) {
        User updatedUser = findUser(id);

        updatedUser.setRoles(rolesDto.getRoles());

        LOGGER.info(ROLES_ASSIGN);

        return UserConverter.convertUserToUserDto(userRepository.save(updatedUser));
    }

    /**
     * Updates the user data
     *
     * @param id
     * @param updateUserDto
     * @return the user updated and saved
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto update(String id, UpdateUserDto updateUserDto) {
        User userEntity = UserConverter.convertUpdateUserDtoToUser(updateUserDto);

        User updatedUser = findUser(id);

        if (userEntity.getName() != null && !userEntity.getName().equals(updatedUser.getName())) {
            updatedUser.setName(userEntity.getName());
        }
        if (userEntity.getEmail() != null && !userEntity.getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(userEntity.getEmail());
        }
        if (userEntity.getPassword() != null && !bcryptEncoder.matches(userEntity.getPassword(),
                updatedUser.getPassword())) {
            updatedUser.setPassword(bcryptEncoder.encode(userEntity.getPassword()));
        }

        LOGGER.info(USER_UPDATED);

        return UserConverter.convertUserToUserDto(userRepository.save(updatedUser));
    }

    /********
     * If user dont have that book as favorite throws exception
     * Gets the book from the list and sets the new list without the taken book
     * Then checks if book doesnt exist in any users list, is removed from database
     * @param id
     * @param userId
     * @return the user updated
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto deleteBookAsFavorite(String id, String userId) {
        bookService.verifyBookExists(id);

        User user = findUser(userId);

        if (user.getFavoriteBooksId() != null && !user.getFavoriteBooksId().contains(id)) {
            throw new BookBadRequestException(NO_FAVORITE);
        }

        List<String> favoriteBooksId = new LinkedList<>(user.getFavoriteBooksId().stream().filter(favoriteBookId ->
                !Objects.equals(favoriteBookId, id)).toList());

        user.setFavoriteBooksId(favoriteBooksId);

        LOGGER.info(REMOVED_FAVORITE_BOOK);

        User userSaved = userRepository.save(user);

        if (Boolean.TRUE.equals(!userRepository.existsByFavoriteBooksId(id)) &&
                Boolean.TRUE.equals(!userRepository.existsByReadBooksId(id))) {
            bookService.delete(id);
        }

        return UserConverter.convertUserToUserDto(userSaved);
    }

    /********
     * If user dont have that book as read throws exception
     * Gets the book from the list and sets the new list without the taken book
     * Then checks if book doesnt exist in any users list, is removed from database
     * @param id
     * @param userId
     * @return the user updated
     */
    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserDto deleteBookAsRead(String id, String userId) {
        bookService.verifyBookExists(id);

        User user = findUser(userId);

        if (user.getReadBooksId() != null && !user.getReadBooksId().contains(id)) {
            throw new BookBadRequestException(NO_READ);
        }

        List<String> readBooksId = new LinkedList<>(user.getReadBooksId().stream().filter(readBookId ->
                !Objects.equals(readBookId, id)).toList());

        user.setReadBooksId(readBooksId);

        LOGGER.info(REMOVED_READ_BOOK);

        User userSaved = userRepository.save(user);

        if (Boolean.TRUE.equals(!userRepository.existsByFavoriteBooksId(id)) &&
                Boolean.TRUE.equals(!userRepository.existsByReadBooksId(id))) {
            bookService.delete(id);
        }

        return UserConverter.convertUserToUserDto(userSaved);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void delete(String id) {
        verifyUserExists(id);

        LOGGER.info(USER_DELETED);

        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User findUser(String id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void verifyUserExists(String id) {
        if (userRepository.existsById(id)) {
            return;
        }

        throw new UsersNotFoundException();
    }

    @Override
    public Boolean existsByFavoriteBooksId(String id) {
        return userRepository.existsByFavoriteBooksId(id);
    }

    @Override
    public Boolean existsByReadBooksId(String id) {
        return userRepository.existsByReadBooksId(id);
    }
}

package academy.mindswap.booksome.controller.book;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static academy.mindswap.booksome.controller.book.BookControllerConstant.*;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.*;
import static academy.mindswap.booksome.util.role.HasRoleTypes.ADMIN;
import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody BookClientDto bookClientDto, BindingResult bindingResult) {
        if (bookClientDto == null) {
            throw new BookBadRequestException(BOOK_NULL);
        }
        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(bookService.save(bookClientDto), HttpStatus.CREATED);
    }

    @GetMapping("/database")
    @PreAuthorize(ADMIN)
    public ResponseEntity<List<BookDto>> findAll() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize(USER)
    public ResponseEntity<List<?>> searchAll(@RequestParam Map<String, String> allParams) {
        if (allParams.isEmpty()) {
            throw new BookBadRequestException(ALL_PARAMS_NULL);
        }
        allParams.forEach((key, value) -> {
            if (key.equals(TITLE) && !value.matches(LETTERS_ONLY)) {
                throw new BookBadRequestException(INVALID_TITLE);
            }
            if (key.equals(AUTHORS) && !value.matches(LETTERS_ONLY)) {
                throw new BookBadRequestException(INVALID_AUTHOR);
            }
            if (key.equals(CATEGORY) && !value.matches(LETTERS_ONLY)) {
                throw new BookBadRequestException(INVALID_CATEGORY);
            }
            if (key.equals(ISBN) && !value.matches(NUMBERS_ONLY)) {
                throw new BookBadRequestException(INVALID_ISBN);
            }
        });

        return new ResponseEntity<>(bookService.searchAll(allParams), HttpStatus.OK);
    }
}


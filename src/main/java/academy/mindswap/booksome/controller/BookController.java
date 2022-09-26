package academy.mindswap.booksome.controller;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.SaveBookDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.service.implementation.BookServiceImpl;
import academy.mindswap.booksome.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static academy.mindswap.booksome.controller.ControllerConstant.*;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.*;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping
    public ResponseEntity<List<?>> findAll(@RequestParam Map<String, String> allParams) {
        if (allParams.isEmpty()) {
            throw new BookBadRequestException(ALL_PARAMS_NULL);
        }
        allParams.forEach((k, v) -> {
            if (k.equals(TITLE) && !v.matches(LETTERS_ONLY))  {
                throw new BookBadRequestException(INVALID_TITLE);
            }
            if (k.equals(AUTHORS) && !v.matches(LETTERS_ONLY)) {
            throw new BookBadRequestException(INVALID_AUTHOR);
            }
            if (k.equals(CATEGORY) && !v.matches(LETTERS_ONLY)) {
                throw new BookBadRequestException(INVALID_CATEGORY);
            }
            if (k.equals(ISBN) && !v.matches(NUMBERS_ONLY)) {
                throw new BookBadRequestException(INVALID_ISBN);
            }
        });

        List<?> bookList = bookService.findAll(allParams);

        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }


}


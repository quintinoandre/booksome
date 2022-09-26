package academy.mindswap.booksome.controller;

import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.service.implementation.BookServiceImpl;
import academy.mindswap.booksome.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static academy.mindswap.booksome.controller.ControllerConstant.*;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.*;
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


package academy.mindswap.booksome.controller.book;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.book.UpdateBookDto;
import academy.mindswap.booksome.dto.swagger.SaveBookDto;
import academy.mindswap.booksome.exception.ExceptionError;
import academy.mindswap.booksome.exception.book.BookBadRequestException;
import academy.mindswap.booksome.service.interfaces.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static academy.mindswap.booksome.controller.book.BookControllerConstant.ISBN;
import static academy.mindswap.booksome.controller.book.BookControllerPattern.ISBN_PATTERN;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.*;
import static academy.mindswap.booksome.util.role.HasRoleTypes.ADMIN;
import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

@Tag(name = "Books", description = "Contains all the operations that can be performed on books.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Save a new book (⚠️ admin users only)", description = "Save a new book in the application " +
            "database.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation =
            SaveBookDto.class)))
    @PostMapping
    @PreAuthorize(ADMIN)
    public ResponseEntity<?> save(@Valid @RequestBody BookClientDto bookClientDto, BindingResult bindingResult) {
        if (bookClientDto == null) {
            throw new BookBadRequestException(BOOK_NULL);
        }
        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(bookService.save(bookClientDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Find a book (⚠️ admin users only)", description = "Find a book by id.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<BookDto> findById(@PathVariable String id) {
        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Find all books in the database (⚠️ admin users only)", description = "Find all books in the" +
            " application database.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation =
            BookDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @GetMapping("/database")
    @PreAuthorize(ADMIN)
    public ResponseEntity<List<BookDto>> findAll() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Search books in the external API", description = "Search books in the external API by " +
            "title, category, author, and/or ISBN.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation =
            SaveBookDto.class))))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @GetMapping
    @PreAuthorize(USER)
    public ResponseEntity<List<?>> searchAll(@Parameter(hidden = true) @RequestParam Map<String, String> allParams,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) String authors,
                                             @RequestParam(required = false) String category,
                                             @RequestParam(required = false) String isbn) {
        if (allParams.isEmpty()) {
            throw new BookBadRequestException(ALL_PARAMS_NULL);
        }
        allParams.forEach((key, value) -> {
            if (key.equals(ISBN) && !value.matches(ISBN_PATTERN)) {
                throw new BookBadRequestException(INVALID_ISBN);
            }
        });

        return new ResponseEntity<>(bookService.searchAll(allParams), HttpStatus.OK);
    }

    @Operation(summary = "Update a book (⚠️ admin users only)", description = "Update a book in the application" +
            " database.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation =
            academy.mindswap.booksome.dto.swagger.UpdateBookDto.class)))
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody UpdateBookDto updateBookDto,
                                    BindingResult bindingResult
    ) {
        if (updateBookDto == null) {
            throw new BookBadRequestException(BOOK_NULL);
        }
        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(bookService.update(id, updateBookDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete a book (⚠️ admin users only)", description = "Delete a book by id.")
    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @DeleteMapping("/{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (id == null) {
            throw new BookBadRequestException(BOOK_ID_NULL);
        }

        bookService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


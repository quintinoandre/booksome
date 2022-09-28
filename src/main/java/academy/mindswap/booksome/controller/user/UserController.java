package academy.mindswap.booksome.controller.user;

import academy.mindswap.booksome.dto.book.BookDto;
import academy.mindswap.booksome.dto.user.RolesDto;
import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.exception.ExceptionError;
import academy.mindswap.booksome.exception.user.UserBadRequestException;
import academy.mindswap.booksome.service.interfaces.UserService;
import academy.mindswap.booksome.util.request.RequestHandler;
import io.swagger.v3.oas.annotations.Operation;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.BOOK_ID_NULL;
import static academy.mindswap.booksome.exception.book.BookExceptionMessage.BOOK_ISBN_NULL;
import static academy.mindswap.booksome.exception.user.UserExceptionMessage.*;
import static academy.mindswap.booksome.util.role.HasRoleTypes.ADMIN;
import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

@Tag(name = "Users", description = "Contains all the operations that can be performed on users.")
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final RequestHandler requestHandler;

    @Autowired
    public UserController(UserService userService, RequestHandler requestHandler) {
        this.userService = userService;
        this.requestHandler = requestHandler;
    }

    @Operation(summary = "Save a new user", description = "Save a new user in the application database.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation =
            academy.mindswap.booksome.dto.swagger.SaveUserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody SaveUserDto saveUserDto,
                                  BindingResult bindingResult) {
        if (saveUserDto == null) {
            throw new UserBadRequestException(USER_NULL);
        }
        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(userService.save(saveUserDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Find all users (⚠️ admin users only)", description = "Find all users in the application " +
            "database.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation =
            UserDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    @PreAuthorize(ADMIN)
    public ResponseEntity<List<UserDto>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Find a favorite book", description = "Find a favorite book by id.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/favoritebook/{id}")
    @PreAuthorize(USER)
    public ResponseEntity<BookDto> findFavoriteBook(HttpServletRequest request, @PathVariable String id) {
        return new ResponseEntity<>(userService.findFavoriteBook(id, requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @Operation(summary = "Find a read book", description = "Find a read book by id.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/readbook/{id}")
    @PreAuthorize(USER)
    public ResponseEntity<BookDto> findReadBook(HttpServletRequest request, @PathVariable String id) {
        return new ResponseEntity<>(userService.findReadBook(id, requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @Operation(summary = "Find all favorite books", description = "Find all books saved as favorite.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation =
            BookDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/favoritebooks")
    @PreAuthorize(USER)
    public ResponseEntity<List<BookDto>> findFavoriteBooks(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findFavoriteBooks(requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @Operation(summary = "Find all read books", description = "Find all books saved as read.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation =
            BookDto.class))))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/readbooks")
    @PreAuthorize(USER)
    public ResponseEntity<List<BookDto>> findReadBooks(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findReadBooks(requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @Operation(summary = "Find a user", description = "Find a user by id.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> findById(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findById(requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @Operation(summary = "Save a book as favorite", description = "Save a book as favorite by ISBN.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/book/{isbn}/favorites")
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> saveBookAsFavorite(HttpServletRequest request, @PathVariable String isbn) {
        if (isbn == null) {
            throw new UserBadRequestException(BOOK_ISBN_NULL);
        }

        return new ResponseEntity<>(userService.saveBookAsFavorite(isbn, requestHandler.getUserId(request)),
                HttpStatus.OK);
    }

    @Operation(summary = "Save a book as read", description = "Save a book as read by ISBN.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/book/{isbn}/read")
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> saveBookAsRead(HttpServletRequest request, @PathVariable String isbn) {
        if (isbn == null) {
            throw new UserBadRequestException(BOOK_ISBN_NULL);
        }

        return new ResponseEntity<>(userService.saveBookAsRead(isbn, requestHandler.getUserId(request)),
                HttpStatus.OK);
    }

    @Operation(summary = "Assign roles (⚠️ admin users only)", description = "Assign roles to a user.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/roles")
    @PreAuthorize(ADMIN)
    public ResponseEntity<?> assignRoles(@PathVariable String id, @Valid @RequestBody RolesDto rolesDto,
                                         BindingResult bindingResult) {
        if (id == null) {
            throw new UserBadRequestException(USER_ID_NULL);
        }
        if (rolesDto == null) {
            throw new UserBadRequestException(ROLES_NULL);
        }
        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(userService.assignRoles(id, rolesDto), HttpStatus.OK);
    }

    @Operation(summary = "Update a user", description = "Update a user in the application database.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation =
            academy.mindswap.booksome.dto.swagger.UpdateUserDto.class)))
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    @PreAuthorize(USER)
    public ResponseEntity<?> update(HttpServletRequest request, @Valid @RequestBody UpdateUserDto
            updateUserDto, BindingResult bindingResult
    ) {
        if (updateUserDto == null) {
            throw new UserBadRequestException(USER_NULL);
        }
        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(userService.update(requestHandler.getUserId(request), updateUserDto),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete a book from favorite books list", description = "Delete a book from favorite books " +
            "list by id.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/book/{id}/favorites")
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> deleteBookAsFavorite(HttpServletRequest request, @PathVariable String id) {
        if (id == null) {
            throw new UserBadRequestException(BOOK_ID_NULL);
        }

        return new ResponseEntity<>(userService.deleteBookAsFavorite(id, requestHandler.getUserId(request)),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete a book from read books list", description = "Delete a book from read books list by id.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserDto.class)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/book/{id}/read")
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> deleteBookAsRead(HttpServletRequest request, @PathVariable String id) {
        if (id == null) {
            throw new UserBadRequestException(BOOK_ID_NULL);
        }

        return new ResponseEntity<>(userService.deleteBookAsRead(id, requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @Operation(summary = "Delete a user (⚠️ admin users only)", description = "Delete a user by id.")
    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ExceptionError.class)))
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize(ADMIN)
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (id == null) {
            throw new UserBadRequestException(USER_ID_NULL);
        }

        userService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

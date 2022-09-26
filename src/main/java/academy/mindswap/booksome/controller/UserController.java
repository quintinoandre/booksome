package academy.mindswap.booksome.controller;

import academy.mindswap.booksome.dto.user.RolesDto;
import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.exception.user.UserBadRequestException;
import academy.mindswap.booksome.service.interfaces.UserService;
import academy.mindswap.booksome.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static academy.mindswap.booksome.exception.book.BookExceptionMessage.BOOK_ISBN_NULL;
import static academy.mindswap.booksome.exception.user.UserExceptionMessage.*;
import static academy.mindswap.booksome.util.role.HasRoleTypes.ADMIN;
import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

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

    @GetMapping("/all")
    @PreAuthorize(ADMIN)
    public ResponseEntity<List<UserDto>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> findById(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findById(requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @PutMapping("/book/{isbn}/favorites")
    public ResponseEntity<UserDto> saveBookAsFavorite(HttpServletRequest request, @PathVariable String isbn) {
        if (isbn == null) {
            throw new UserBadRequestException(BOOK_ISBN_NULL);
        }

        return new ResponseEntity<>(userService.saveBookAsFavorite(isbn, requestHandler.getUserId(request)),
                HttpStatus.OK);
    }

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

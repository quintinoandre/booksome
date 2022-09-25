package academy.mindswap.booksome.controller;

import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.exception.user.UserBadRequestException;
import academy.mindswap.booksome.service.interfaces.UserService;
import academy.mindswap.booksome.util.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static academy.mindswap.booksome.exception.user.UserExceptionMessage.USER_NULL;
import static academy.mindswap.booksome.util.role.HasRoleTypes.ADMIN;
import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;
import static academy.mindswap.booksome.util.validation.PrintValidationError.printValidationError;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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
            LOGGER.error(USER_NULL);

            throw new UserBadRequestException(USER_NULL);
        }

        if (bindingResult.hasErrors()) {
            return printValidationError(bindingResult);
        }

        return new ResponseEntity<>(userService.save(saveUserDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize(USER)
    public ResponseEntity<UserDto> findById(HttpServletRequest request) {
        return new ResponseEntity<>(userService.findById(requestHandler.getUserId(request)), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize(ADMIN)
    public ResponseEntity<List<UserDto>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}

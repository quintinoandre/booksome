package academy.mindswap.booksome.controller;

import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.service.interfaces.UserService;
import academy.mindswap.booksome.util.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static academy.mindswap.booksome.util.role.HasRoleTypes.ADMIN;
import static academy.mindswap.booksome.util.role.HasRoleTypes.USER;

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

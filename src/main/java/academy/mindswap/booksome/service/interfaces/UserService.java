package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    User findByEmail(String email);

}

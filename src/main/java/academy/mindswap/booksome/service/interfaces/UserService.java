package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.model.User;

import java.util.List;

public interface UserService {
    UserDto save(SaveUserDto saveUserDto);

    UserDto findById(String id);

    List<UserDto> findAll();

    User findUser(String id);

    User findByEmail(String email);
}

package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.user.RolesDto;
import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.model.User;

import java.util.List;

public interface UserService {
    UserDto save(SaveUserDto saveUserDto);

    List<UserDto> findAll();

    UserDto findById(String id);

    UserDto assignRoles(String id, RolesDto rolesDto);

    User findUser(String id);

    User findByEmail(String email);

}

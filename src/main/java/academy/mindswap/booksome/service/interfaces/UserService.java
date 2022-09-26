package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.dto.user.RolesDto;
import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.model.User;

import java.util.List;

public interface UserService {
    UserDto save(SaveUserDto saveUserDto);

    List<UserDto> findAll();

    UserDto findById(String id);

    UserDto assignRoles(String id, RolesDto rolesDto);

    UserDto update(String id, UpdateUserDto updateUserDto);

    void delete(String id);

    User findUser(String id);

    User findByEmail(String email);

    void verifyUserExists(String id);
}

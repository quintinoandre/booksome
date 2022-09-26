package academy.mindswap.booksome.converter;

import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.model.User;
import org.modelmapper.ModelMapper;

public final class UserConverter {
    private UserConverter() {
    }

    public static UserDto convertUserToUserDto(User user) {
        return new ModelMapper().map(user, UserDto.class);
    }

    public static User convertUpdateUserDtoToUser(UpdateUserDto updateUserDto) {
        return new ModelMapper().map(updateUserDto, User.class);
    }

    public static User convertSaveUserDtoToUser(SaveUserDto saveUserDto) {
        return new ModelMapper().map(saveUserDto, User.class);
    }
}

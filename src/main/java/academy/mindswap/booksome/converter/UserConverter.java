package academy.mindswap.booksome.converter;

import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.model.User;
import org.modelmapper.ModelMapper;

public class UserConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserDto convertUserToUserDto (User user){
        return modelMapper.map(user, UserDto.class);

    }
    public static User convertUserDtoToUser (UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    public static User convertUpdateUserDtoToUser(UpdateUserDto updateUserDto){
        return modelMapper.map(updateUserDto, User.class);
    }

    public static User convertSaveUserDtoToUser(SaveUserDto saveUserDto){
        return modelMapper.map(saveUserDto, User.class);
    }
}

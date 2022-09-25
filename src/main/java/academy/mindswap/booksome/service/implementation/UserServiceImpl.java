package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.converter.UserConverter;
import academy.mindswap.booksome.dto.user.RolesDto;
import academy.mindswap.booksome.dto.user.SaveUserDto;
import academy.mindswap.booksome.dto.user.UpdateUserDto;
import academy.mindswap.booksome.dto.user.UserDto;
import academy.mindswap.booksome.exception.user.UserBadRequestException;
import academy.mindswap.booksome.exception.user.UserNotFoundException;
import academy.mindswap.booksome.exception.user.UsersNotFoundException;
import academy.mindswap.booksome.model.User;
import academy.mindswap.booksome.repository.UserRepository;
import academy.mindswap.booksome.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static academy.mindswap.booksome.exception.user.UserExceptionMessage.EMAIL_ALREADY_EXISTS;
import static academy.mindswap.booksome.util.user.UserMessage.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    private void verifyEmailExits(String email) {
        boolean userExists = userRepository.existsByEmail(email);

        if (userExists) {
            throw new UserBadRequestException(EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public UserDto save(SaveUserDto saveUserDto) {
        User userEntity = UserConverter.convertSaveUserDtoToUser(saveUserDto);

        verifyEmailExits(userEntity.getEmail());

        userEntity.setPassword(bcryptEncoder.encode(userEntity.getPassword()));

        LOGGER.info(USER_SAVED);

        return UserConverter.convertUserToUserDto(userRepository.save(userEntity));
    }

    @Override
    public List<UserDto> findAll() {
        List<User> usersEntities = userRepository.findAll();

        if (usersEntities.isEmpty()) {
            throw new UsersNotFoundException();
        }

        LOGGER.info(USERS_FOUND);

        return usersEntities.stream().map(UserConverter::convertUserToUserDto).toList();
    }

    @Override
    public UserDto findById(String id) {
        User userEntity = findUser(id);

        LOGGER.info(USER_FOUND);

        return UserConverter.convertUserToUserDto(userEntity);
    }

    @Override
    public UserDto assignRoles(String id, RolesDto rolesDto) {
        User updatedUser = findUser(id);

        updatedUser.setRoles(rolesDto.getRoles());

        LOGGER.info(ROLES_ASSIGN);

        return UserConverter.convertUserToUserDto(userRepository.save(updatedUser));
    }

    @Override
    public UserDto update(String id, UpdateUserDto updateUserDto) {
        User userEntity = UserConverter.convertUpdateUserDtoToUser(updateUserDto);

        User updatedUser = findUser(id);

        if (userEntity.getName() != null && !userEntity.getName().equals(updatedUser.getName())) {
            updatedUser.setName(userEntity.getName());
        }

        if (userEntity.getEmail() != null && !userEntity.getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(userEntity.getEmail());
        }

        if (userEntity.getPassword() != null && !bcryptEncoder.matches(userEntity.getPassword(),
                updatedUser.getPassword())) {
            updatedUser.setPassword(bcryptEncoder.encode(userEntity.getPassword()));
        }

        LOGGER.info(USER_UPDATED);

        return UserConverter.convertUserToUserDto(userRepository.save(updatedUser));
    }

    @Override
    public User findUser(String id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}

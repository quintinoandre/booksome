package academy.mindswap.booksome.service.implementation;

import academy.mindswap.booksome.converter.UserConverter;
import academy.mindswap.booksome.dto.user.UserDto;
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

import static academy.mindswap.booksome.exception.user.UserExceptionMessage.USERS_NOT_FOUND;
import static academy.mindswap.booksome.util.user.UserMessage.USERS_FOUND;
import static academy.mindswap.booksome.util.user.UserMessage.USER_FOUND;

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

    @Override
    public UserDto findById(String id) {
        User userEntity = findUser(id);

        LOGGER.info(USER_FOUND);

        return UserConverter.convertUserToUserDto(userEntity);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> usersEntities = userRepository.findAll();

        if (usersEntities.isEmpty()) {
            LOGGER.error(USERS_NOT_FOUND);

            throw new UsersNotFoundException();
        }

        LOGGER.info(USERS_FOUND);

        return usersEntities.stream().map(UserConverter::convertUserToUserDto).toList();
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

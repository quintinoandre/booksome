package academy.mindswap.booksome.config;

import academy.mindswap.booksome.model.User;
import academy.mindswap.booksome.repository.UserRepository;
import academy.mindswap.booksome.util.role.RoleTypes;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Slf4j
public class DataLoaderConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoaderConfig.class);

    private static void saveUser(UserRepository userRepository, String email, User user) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            LOGGER.error("User with email ({}) already exists", email);
        } else {
            LOGGER.info("Inserting {} user", user.getName());

            userRepository.insert(user);
        }
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder bcryptEncoder) {
        return args -> {
            String email1 = "ivan.moreira@mindswap.academy";

            User user1 = User
                    .builder()
                    .name("Ivan Moreira")
                    .email(email1)
                    .password(bcryptEncoder.encode("1234"))
                    .roles(List.of(RoleTypes.ADMIN, RoleTypes.USER))
                    .build();

            saveUser(userRepository, email1, user1);

            String email2 = "andre.quintino@mindswap.academy";

            User user2 = User
                    .builder()
                    .name("André Quintino")
                    .email(email2)
                    .password(bcryptEncoder.encode("1234"))
                    .roles(List.of(RoleTypes.USER))
                    .build();

            saveUser(userRepository, email2, user2);

            String email3 = "lygia.garrido@mindswap.academy";

            User user3 = User
                    .builder()
                    .name("Lygia Garrido")
                    .email(email3)
                    .password(bcryptEncoder.encode("1234"))
                    .roles(List.of(RoleTypes.USER))
                    .build();

            saveUser(userRepository, email3, user3);
        };
    }
}

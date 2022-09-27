package academy.mindswap.booksome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
public class BooksomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooksomeApplication.class, args);
    }
}

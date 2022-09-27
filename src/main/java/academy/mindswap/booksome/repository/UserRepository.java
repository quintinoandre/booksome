package academy.mindswap.booksome.repository;

import academy.mindswap.booksome.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByFavoriteBooksId(String id);

    Boolean existsByReadBooksId(String id);
}

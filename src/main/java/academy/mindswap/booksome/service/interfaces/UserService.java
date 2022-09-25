package academy.mindswap.booksome.service.interfaces;

import academy.mindswap.booksome.model.User;

public interface UserService {
    User findByEmail(String email);
}

package academy.mindswap.booksome.exception;

public class NotFoundException extends RuntimeException {
    protected NotFoundException(String message) {
        super(message);
    }
}

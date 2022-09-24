package academy.mindswap.booksome.exception;

public class BadRequestException extends RuntimeException {
    protected BadRequestException(String message) {
        super(message);
    }
}

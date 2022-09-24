package academy.mindswap.booksome.exception.client;

public class Client4xxErrorException extends RuntimeException {
    public Client4xxErrorException(String message) {
        super(message);
    }
}

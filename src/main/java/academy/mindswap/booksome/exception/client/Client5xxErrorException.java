package academy.mindswap.booksome.exception.client;

public final class Client5xxErrorException extends RuntimeException {
    public Client5xxErrorException(String message) {
        super(message);
    }
}

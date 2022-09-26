package academy.mindswap.booksome.exception.jwt;

public final class JwtExceptionMessage {
    private JwtExceptionMessage() {
    }

    public static final String INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String USER_DISABLED = "User disable.";
    public static final String AUTHENTICATION_NULL = "Authentication object cannot be null.";
    public static final String REQUEST_NULL = "Request object cannot be null.";
    public static final String UNAUTHORIZED = "Unauthorized.";
    public static final String CANNOT_SET_SECURITY_CONTEXT = "Cannot set the Security Context.";
}

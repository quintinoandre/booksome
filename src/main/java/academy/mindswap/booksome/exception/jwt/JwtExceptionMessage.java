package academy.mindswap.booksome.exception.jwt;

public final class JwtExceptionMessage {
    private JwtExceptionMessage() {
    }

    public static final String INVALID_CREDENTIALS = "INVALID CREDENTIALS";
    public static final String USER_DISABLED = "USER DISABLED";
    public static final String AUTHENTICATION_NULL = "object authentication is null";
    public static final String REQUEST_NULL = "object request is null";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String CANNOT_SET_SECURITY_CONTEXT = "Cannot set the Security Context";
}

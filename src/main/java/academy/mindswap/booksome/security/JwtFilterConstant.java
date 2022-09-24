package academy.mindswap.booksome.security;

public final class JwtFilterConstant {
    private JwtFilterConstant() {
    }

    static final String BEARER_PREFIX = "Bearer ";
    static final String CLAIMS = "claims";
    static final String EXCEPTION = "exception";
    static final String IS_REFRESH_TOKEN = "isRefreshToken";
    static final String REFRESH_TOKEN = "refreshtoken";
    static final String TRUE = "true";
}

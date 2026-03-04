package gift.auth;

public interface AuthService {
    String buildAuthorizationUrl();

    TokenResponse processCallback(String code);
}

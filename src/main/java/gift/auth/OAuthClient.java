package gift.auth;

public interface OAuthClient {
    String buildAuthorizationUrl();

    OAuthResult authenticate(String code);
}

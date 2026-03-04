package gift.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoOAuthClient implements OAuthClient {
    private final KakaoLoginProperties properties;
    private final RestClient restClient;

    public KakaoOAuthClient(KakaoLoginProperties properties, RestClient.Builder builder) {
        this.properties = properties;
        this.restClient = builder.build();
    }

    @Override
    public String buildAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUri())
            .queryParam("scope", "account_email,talk_message")
            .build()
            .toUriString();
    }

    @Override
    public OAuthResult authenticate(String code) {
        KakaoTokenResponse token = requestAccessToken(code);
        KakaoUserResponse user = requestUserInfo(token);
        return new OAuthResult(user.email(), token.accessToken());
    }

    private KakaoTokenResponse requestAccessToken(String code) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", properties.clientId());
        params.add("redirect_uri", properties.redirectUri());
        params.add("code", code);
        params.add("client_secret", properties.clientSecret());

        return restClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .body(params)
            .retrieve()
            .body(KakaoTokenResponse.class);
    }

    private KakaoUserResponse requestUserInfo(KakaoTokenResponse tokenResponse) {
        return restClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header("Authorization", "Bearer " + tokenResponse.accessToken())
            .retrieve()
            .body(KakaoUserResponse.class);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record KakaoTokenResponse(@JsonProperty("access_token") String accessToken) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record KakaoUserResponse(@JsonProperty("kakao_account") KakaoAccount kakaoAccount) {

        public String email() {
            return kakaoAccount.email();
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record KakaoAccount(String email) {
        }
    }
}

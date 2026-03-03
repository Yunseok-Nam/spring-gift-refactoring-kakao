package gift.auth;

import gift.member.Member;
import gift.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoAuthService {
    private final KakaoLoginProperties properties;
    private final KakaoLoginClient kakaoLoginClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public KakaoAuthService(
        KakaoLoginProperties properties,
        KakaoLoginClient kakaoLoginClient,
        MemberRepository memberRepository,
        JwtProvider jwtProvider
    ) {
        this.properties = properties;
        this.kakaoLoginClient = kakaoLoginClient;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String buildAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", properties.clientId())
            .queryParam("redirect_uri", properties.redirectUri())
            .queryParam("scope", "account_email,talk_message")
            .build()
            .toUriString();
    }

    public TokenResponse processCallback(String code) {
        KakaoLoginClient.KakaoTokenResponse kakaoToken = kakaoLoginClient.requestAccessToken(code);
        KakaoLoginClient.KakaoUserResponse kakaoUser = kakaoLoginClient.requestUserInfo(kakaoToken.accessToken());
        String email = kakaoUser.email();

        var member = memberRepository.findByEmail(email)
            .orElseGet(() -> new Member(email));
        member.updateKakaoAccessToken(kakaoToken.accessToken());
        memberRepository.save(member);

        var token = jwtProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }
}

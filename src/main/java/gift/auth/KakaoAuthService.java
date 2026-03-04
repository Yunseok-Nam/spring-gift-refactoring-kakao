package gift.auth;

import gift.member.Member;
import gift.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService implements AuthService {
    private final OAuthClient oAuthClient;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public KakaoAuthService(
        OAuthClient oAuthClient,
        MemberRepository memberRepository,
        JwtProvider jwtProvider
    ) {
        this.oAuthClient = oAuthClient;
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String buildAuthorizationUrl() {
        return oAuthClient.buildAuthorizationUrl();
    }

    @Override
    public TokenResponse processCallback(String code) {
        OAuthResult result = oAuthClient.authenticate(code);

        Member member = findOrCreateMember(result.email());
        member.updateKakaoAccessToken(result.accessToken());
        memberRepository.save(member);

        return new TokenResponse(jwtProvider.createToken(member));
    }

    private Member findOrCreateMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseGet(() -> new Member(email));
    }
}

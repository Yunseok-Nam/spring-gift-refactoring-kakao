package gift.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        var kakaoAuthUrl = kakaoAuthService.buildAuthorizationUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, kakaoAuthUrl)
            .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenResponse> callback(@RequestParam("code") String code) {
        var tokenResponse = kakaoAuthService.processCallback(code);
        return ResponseEntity.ok(tokenResponse);
    }
}

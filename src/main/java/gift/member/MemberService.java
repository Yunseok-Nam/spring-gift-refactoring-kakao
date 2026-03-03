package gift.member;

import gift.auth.JwtProvider;
import gift.auth.TokenResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public TokenResponse register(MemberRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        var member = memberRepository.save(new Member(request.email(), request.password()));
        var token = jwtProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }

    public TokenResponse login(MemberRequest request) {
        var member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (member.getPassword() == null || !member.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        var token = jwtProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + id));
    }

    public Member create(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        return memberRepository.save(new Member(email, password));
    }

    public Member update(Long id, String email, String password) {
        var member = findById(id);
        member.update(email, password);
        return memberRepository.save(member);
    }

    public void chargePoint(Long id, int amount) {
        var member = findById(id);
        member.chargePoint(amount);
        memberRepository.save(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}

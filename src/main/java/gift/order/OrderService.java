package gift.order;

import gift.member.Member;
import gift.member.MemberRepository;
import gift.option.Option;
import gift.option.OptionRepository;
import gift.wish.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final KakaoMessageClient kakaoMessageClient;

    public OrderService(
        OrderRepository orderRepository,
        OptionRepository optionRepository,
        WishRepository wishRepository,
        MemberRepository memberRepository,
        KakaoMessageClient kakaoMessageClient
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.kakaoMessageClient = kakaoMessageClient;
    }

    public Page<Order> findByMemberId(Long memberId, Pageable pageable) {
        return orderRepository.findByMemberId(memberId, pageable);
    }

    // order flow:
    // 1. auth check
    // 2. validate option
    // 3. subtract stock
    // 4. deduct points
    // 5. save order
    // 6. cleanup wish
    // 7. send kakao notification
    public Order create(Member member, OrderRequest request) {
        // validate option
        var option = optionRepository.findById(request.optionId()).orElse(null);
        if (option == null) {
            return null;
        }

        // subtract stock
        option.subtractQuantity(request.quantity());
        optionRepository.save(option);

        // deduct points
        var price = option.getProduct().getPrice() * request.quantity();
        member.deductPoint(price);
        memberRepository.save(member);

        // save order
        var saved = orderRepository.save(new Order(option, member.getId(), request.quantity(), request.message()));

        // best-effort kakao notification
        sendKakaoMessageIfPossible(member, saved, option);
        return saved;
    }

    private void sendKakaoMessageIfPossible(Member member, Order order, Option option) {
        if (member.getKakaoAccessToken() == null) {
            return;
        }
        try {
            var product = option.getProduct();
            kakaoMessageClient.sendToMe(member.getKakaoAccessToken(), order, product);
        } catch (Exception ignored) {
        }
    }
}

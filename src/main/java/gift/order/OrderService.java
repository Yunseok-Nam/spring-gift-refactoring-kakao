package gift.order;

import gift.member.Member;
import gift.member.MemberRepository;
import gift.option.Option;
import gift.option.OptionRepository;
import gift.wish.WishRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(
        OrderRepository orderRepository,
        OptionRepository optionRepository,
        WishRepository wishRepository,
        MemberRepository memberRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
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
    @Transactional
    public Order create(Member member, OrderRequest request) {
        // validate option
        Option option = optionRepository.findById(request.optionId())
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다. id=" + request.optionId()));

        // subtract stock
        option.subtractQuantity(request.quantity());

        // deduct points
        int totalPrice = option.calculateTotalPrice(request.quantity());
        member.deductPoint(totalPrice);

        // save order
        Order saved = orderRepository.save(new Order(option, member.getId(), request.quantity(), request.message()));

        // cleanup wish
        Long productId = option.getProduct().getId();
        wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .ifPresent(wishRepository::delete);

        // publish event for best-effort kakao notification (sent after commit)
        if (member.hasKakaoAccount()) {
            eventPublisher.publishEvent(
                new OrderCompletedEvent(member.getKakaoAccessToken(), saved, option.getProduct()));
        }
        return saved;
    }
}

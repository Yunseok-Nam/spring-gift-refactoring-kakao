package gift.order;

import gift.member.Member;
import gift.member.MemberRepository;
import gift.option.Option;
import gift.option.OptionRepository;
import gift.wish.WishRepository;
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
    private final MessageClient messageClient;

    public OrderService(
        OrderRepository orderRepository,
        OptionRepository optionRepository,
        WishRepository wishRepository,
        MemberRepository memberRepository,
        MessageClient messageClient
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.messageClient = messageClient;
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
        optionRepository.save(option);

        // deduct points
        int totalPrice = option.calculateTotalPrice(request.quantity());
        member.deductPoint(totalPrice);
        memberRepository.save(member);

        // save order
        Order saved = orderRepository.save(new Order(option, member.getId(), request.quantity(), request.message()));

        // cleanup wish
        Long productId = option.getProduct().getId();
        wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .ifPresent(wishRepository::delete);

        // best-effort kakao notification
        sendMessageIfPossible(member, saved, option);
        return saved;
    }

    private void sendMessageIfPossible(Member member, Order order, Option option) {
        if (!member.hasKakaoAccount()) {
            return;
        }
        try {
            messageClient.sendOrderMessage(member.getKakaoAccessToken(), order, option.getProduct());
        } catch (Exception ignored) {
        }
    }
}

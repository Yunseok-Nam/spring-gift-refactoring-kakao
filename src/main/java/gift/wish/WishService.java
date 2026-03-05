package gift.wish;

import gift.product.Product;
import gift.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public Page<Wish> findByMemberId(Long memberId, Pageable pageable) {
        return wishRepository.findByMemberId(memberId, pageable);
    }

    public WishAddResult add(Long memberId, WishRequest request) {
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다. id=" + request.productId()));

        Optional<Wish> existing = wishRepository.findByMemberIdAndProductId(memberId, product.getId());
        if (existing.isPresent()) {
            return new WishAddResult(existing.get(), false);
        }

        Wish saved = wishRepository.save(new Wish(memberId, product));
        return new WishAddResult(saved, true);
    }

    public record WishAddResult(Wish wish, boolean created) {
    }

    public void remove(Long memberId, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new NoSuchElementException("위시가 존재하지 않습니다. id=" + wishId));
        if (!wish.isOwnedBy(memberId)) {
            throw new IllegalStateException("본인의 위시만 삭제할 수 있습니다.");
        }
        wishRepository.delete(wish);
    }
}

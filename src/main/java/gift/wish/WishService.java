package gift.wish;

import gift.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        var product = productRepository.findById(request.productId()).orElse(null);
        if (product == null) {
            return null;
        }

        var existing = wishRepository.findByMemberIdAndProductId(memberId, product.getId()).orElse(null);
        if (existing != null) {
            return new WishAddResult(existing, false);
        }

        var saved = wishRepository.save(new Wish(memberId, product));
        return new WishAddResult(saved, true);
    }

    public record WishAddResult(Wish wish, boolean created) {
    }

    public WishDeleteResult remove(Long memberId, Long wishId) {
        var wish = wishRepository.findById(wishId).orElse(null);
        if (wish == null) {
            return WishDeleteResult.NOT_FOUND;
        }
        if (!wish.getMemberId().equals(memberId)) {
            return WishDeleteResult.FORBIDDEN;
        }
        wishRepository.delete(wish);
        return WishDeleteResult.DELETED;
    }

    public enum WishDeleteResult {
        DELETED, NOT_FOUND, FORBIDDEN
    }
}

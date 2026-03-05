package gift.order;

import gift.product.Product;

public record OrderCompletedEvent(
    String kakaoAccessToken,
    Order order,
    Product product
) {
}

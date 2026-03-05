package gift.order;

import gift.product.Product;

public interface MessageClient {
    void sendOrderMessage(String accessToken, Order order, Product product);
}

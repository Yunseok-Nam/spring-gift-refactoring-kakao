package gift.order;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderNotificationListener {
    private final MessageClient messageClient;

    public OrderNotificationListener(MessageClient messageClient) {
        this.messageClient = messageClient;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCompleted(OrderCompletedEvent event) {
        try {
            messageClient.sendOrderMessage(event.kakaoAccessToken(), event.order(), event.product());
        } catch (Exception ignored) {
        }
    }
}

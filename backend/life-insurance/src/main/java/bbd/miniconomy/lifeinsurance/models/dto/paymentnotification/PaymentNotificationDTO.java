package bbd.miniconomy.lifeinsurance.models.dto.paymentnotification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentNotificationDTO {
    private String type;
    private PaymentNotificationTransactionDTO transaction;
}

package bbd.miniconomy.lifeinsurance.models.dto.paymentnotification;

import bbd.miniconomy.lifeinsurance.enums.PaymentNotificationTransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentNotificationDTO {
    private PaymentNotificationTransactionType type;
    private PaymentNotificationTransactionDTO transaction;
}
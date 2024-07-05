package bbd.miniconomy.lifeinsurance.models.dto.paymentnotification;

import bbd.miniconomy.lifeinsurance.enums.PaymentNotificationTransactionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentNotificationDTO {
    private PaymentNotificationTransactionType type;
    private PaymentNotificationTransactionDTO transaction;
}
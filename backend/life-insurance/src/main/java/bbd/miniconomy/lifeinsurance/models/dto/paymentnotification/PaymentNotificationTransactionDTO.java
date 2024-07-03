package bbd.miniconomy.lifeinsurance.models.dto.paymentnotification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentNotificationTransactionDTO {
    private String id;
    private Long amount;
    private String debitAccountName;
    private String creditAccountName;
    private String errorMessage;
}
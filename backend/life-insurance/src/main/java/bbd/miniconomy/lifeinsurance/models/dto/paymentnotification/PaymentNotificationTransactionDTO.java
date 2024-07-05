package bbd.miniconomy.lifeinsurance.models.dto.paymentnotification;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentNotificationTransactionDTO {
    private String id;
    private String debitAccountName;
    private String creditAccountName;
    private Long amount;
    private String status;
    private String reference;
    private String date;
}
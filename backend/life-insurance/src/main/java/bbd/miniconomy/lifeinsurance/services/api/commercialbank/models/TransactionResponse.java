package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
        private String debitorAccName;
        private String creditorAccName;
        private Long amount;
        private PaymentStatus status;
        private String debitRef;
        private String creditRef;
        private String date;
}

package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
        private String transactionId;
        private String debitAccountName;
        private String creditAccountName;
        private double amount;
        private String status;
        private String debitRef;
        private String creditRef;
        private String date;
}

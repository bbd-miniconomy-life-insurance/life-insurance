package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
        private String debitOrderId;
        private String creditAccountName;
        private String debitAccountName;
        private String debitOrderCreatedDate;
        private double debitOrderAmount;
        private String debitOrderReceiverRef;
        private String debitOrderSenderRef;
        private String disabled;
}

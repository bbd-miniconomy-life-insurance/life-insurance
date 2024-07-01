package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitOrderResponse {
        private UUID id;
        private String debitAccountName;
        private String creditAccountName;
        private String creationDate;
        private Long amount;
        private String senderRef;
        private String receiverRef;
        private boolean disabled;
}

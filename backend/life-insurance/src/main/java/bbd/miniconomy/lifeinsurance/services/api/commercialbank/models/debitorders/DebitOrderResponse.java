package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DebitOrderResponse {
        private String id;
        private String debitAccountName;
        private String creditAccountName;
        private String creationDate;
        private Long amount;
        private String senderRef;
        private String receiverRef;
        private boolean disabled;
}

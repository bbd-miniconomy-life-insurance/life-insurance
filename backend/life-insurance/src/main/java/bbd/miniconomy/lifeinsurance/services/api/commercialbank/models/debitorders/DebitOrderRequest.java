package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DebitOrderRequest {
    private String debitAccountName;
    private String creditAccountName;
    private Long amount;
    private String debitRef;
    private String creditRef;
}


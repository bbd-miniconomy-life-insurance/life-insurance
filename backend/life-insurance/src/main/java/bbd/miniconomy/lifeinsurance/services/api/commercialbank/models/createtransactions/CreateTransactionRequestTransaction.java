package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CreateTransactionRequestTransaction {
    private String debitAccountName;
    private String creditAccountName;
    private Long amount;
    private String debitRef;
    private String creditRef;
}

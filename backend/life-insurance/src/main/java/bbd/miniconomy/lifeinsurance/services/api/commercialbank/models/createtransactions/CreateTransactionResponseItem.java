package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTransactionResponseItem {
    private String id;
    private String debitAccountName;
    private String creditAccountName;
    private Long amount;
    private String status;
    private String debitRef;
    private String creditRef;
    private String date;
}

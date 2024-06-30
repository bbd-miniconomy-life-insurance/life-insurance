package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTransactionResponse {
    private int status;
    private CreateTransactionResponseData data;
    private String message;
}

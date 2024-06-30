package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateTransactionRequest {
    private List<CreateTransactionRequestTransaction> transactions;
}


package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.DebitOrderRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommercialBankService {
    private final APILayer communicationLayer;

    public CommercialBankService(APILayer apiLayer) {
        this.communicationLayer = apiLayer;
    }

    public CreateTransactionResponse createTransactions(CreateTransactionRequest transactions) {
        return communicationLayer
                .getCommercialBankAPI()
                .createTransactions(transactions);
    }

    // Is personaId correct as the accountName?
    public Result<CreateTransactionResponse> createDebitOrder(Long personaId, double amount) {
        // build request
        DebitOrderRequest claimRequest = DebitOrderRequest
                .builder()
                .debitAccountName(personaId.toString())
                .debitOrderAmount(amount)
                .debitOrderReceiverRef("Life Insurance premium from " + personaId)
                .debitOrderSenderRef("Life Insurance premium")
                .build();

        // send request
        return communicationLayer
                .getCommercialBankAPI()
                .createDebitOrder(claimRequest);
    }

    public Result<CreateTransactionResponse> createDebitOrders(List<Long> personaIds, double amount) {
        List<Result<CreateTransactionResponse>> results = personaIds.stream()
            .map(personaId -> {
                // build requests
                DebitOrderRequest claimRequest = DebitOrderRequest
                        .builder()
                        .debitAccountName(personaId.toString())
                        .debitOrderAmount(amount)
                        .debitOrderReceiverRef("Life Insurance premium from " + personaId)
                        .debitOrderSenderRef("Life Insurance premium")
                        .build();

                // send requests
                return communicationLayer
                        .getCommercialBankAPI()
                        .createDebitOrder(claimRequest);
            })
            .collect(Collectors.toList());

        return null;
//        return results;
    }
}

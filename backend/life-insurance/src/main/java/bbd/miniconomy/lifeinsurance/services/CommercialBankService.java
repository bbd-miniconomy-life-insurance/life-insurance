package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.DebitOrderRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.DebitOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommercialBankService {
    private final APILayer communicationLayer;

    public CommercialBankService(APILayer apiLayer) {
        this.communicationLayer = apiLayer;
    }

    public Result<TransactionResponse> createTransaction(Long deceasedPersonaId, Long nextOfKinPersonaId, Long amount) {
        // build request
        TransactionRequest claimRequest = TransactionRequest
                .builder()
                .debitAccountName(nextOfKinPersonaId.toString())
                .transactionAmount(amount)
                .debitRef("Life Insurance Payout from " + deceasedPersonaId + " to " + nextOfKinPersonaId)
                .creditRef("Claim for " + deceasedPersonaId)
                .build();

        // send request
        return communicationLayer
                .getCommercialBankAPI()
                .createTransaction(claimRequest);
    }

    // Is personaId correct as the accountName?
    public Result<TransactionResponse> createDebitOrder(Long personaId, double amount) {
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

    public Result<TransactionResponse> createDebitOrders(List<Long> personaIds, double amount) {
        List<Result<TransactionResponse>> results = personaIds.stream()
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

        return results;
    }
}

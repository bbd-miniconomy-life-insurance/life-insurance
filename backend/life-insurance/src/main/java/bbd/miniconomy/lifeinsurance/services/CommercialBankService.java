package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommercialBankService {
    private final APILayer communicationLayer;

    public CommercialBankService(APILayer apiLayer) {
        this.communicationLayer = apiLayer;
    }

    public Result<TransactionResponse> createTransaction(Long deceasedPersonaId, Long nextOfKinPersonaId, double amount) {
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

}

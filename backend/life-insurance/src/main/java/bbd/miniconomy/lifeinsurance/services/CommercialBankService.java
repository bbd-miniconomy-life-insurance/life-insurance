package bbd.miniconomy.lifeinsurance.services;

import org.springframework.stereotype.Service;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;

@Service
public class CommercialBankService {
    private final APILayer communicationLayer;
    private PriceRepository priceRepository;

    public CommercialBankService(APILayer apiLayer, PriceRepository priceRepository) {
        this.communicationLayer = apiLayer;
        this.priceRepository = priceRepository;
    }

    public Result<CreateTransactionResponse> createTransactions(CreateTransactionRequest transactions) {
        return communicationLayer
                .getCommercialBankAPI()
                .createTransactions(transactions);
    }

    public Result<DebitOrderListResponseTemplate> createDebitOrder(List<Long> personaIds) {
        Long currentPremiumPrice = priceRepository.findFirstByOrderByInceptionDateDesc();

        // build request
        var debitOrderRequests = personaIds
            .stream()
            .map(personaId -> DebitOrderRequest
                    .builder()
                    .debitAccountName("life-insurance")
                    .creditAccountName(personaId.toString())
                    .amount(currentPremiumPrice)
                    .debitRef("Life Insurance premium for " + personaId + " activated")
                    .creditRef("Life Insurance premium")
                    .build();
            )
            .toList()

        var debitOrderCreateRequest = new DebitOrderCreateRequest(debitOrderRequests);

        // send request
        return communicationLayer
                .getCommercialBankAPI()
                .createDebitOrder(debitOrderCreateRequest);
    }

    public Result<DebitOrderResponseTemplate> updateDebitOrder(String debitOrderReferenceNumber, long amount) {
        DebitOrderRequest debitOrderRequest = new DebitOrderRequest
            .Builder()
            .debitOrderReferenceNumber(debitOrderReferenceNumber) // Ensure this builder method is properly defined in DebitOrderRequest.
            .amount(amount)
            // TODO: Add the rest of the fields (debitAccountName, creditAccountName, debitRef, creditRef)
            .build();

        // Call the updateDebitOrder method of the CommercialBankAPI
        // Assuming it returns Result<DebitOrderResponseTemplate> as per its signature.
        return communicationLayer.getCommercialBankAPI().updateDebitOrder(debitOrderRequest);
    }

    // public List<Result<DebitOrderResponseTemplate>> updateDebitOrders(List<Long> personaIds, long amount) {
    //     return personaIds.stream()
    //         .map(personaId -> {
    //             DebitOrderRequest debitOrderRequest = new DebitOrderRequest.Builder()
    //                 .debitAccountName(personaId.toString())
    //                 .creditAccountName("life-insurance")
    //                 .amount(amount)
    //                 .debitRef("Life Insurance premium update for " + personaId)
    //                 .creditRef("Life Insurance premium")
    //                 .build();

    //             // Correctly handle the Result type to match the expected type from the API
    //             Result<DebitOrderResponseTemplate> result = communicationLayer.getCommercialBankAPI().updateDebitOrder(debitOrderRequest);
    //             return result;
    //         })
    //         .collect(Collectors.toList());
    // }
}

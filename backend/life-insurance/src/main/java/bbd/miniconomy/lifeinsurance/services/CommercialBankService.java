package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.lifeevents.LifeEventsDeathDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderCreateRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommercialBankService {
    private final APILayer communicationLayer;
    private final PriceRepository priceRepository;

    public CommercialBankService(APILayer apiLayer, PriceRepository priceRepository) {
        this.communicationLayer = apiLayer;
        this.priceRepository = priceRepository;
    }

    public Result<CreateTransactionResponse> createTransactions(List<LifeEventsDeathDTO> claims) {
        var transactions = claims
                .stream()
                .map(claim -> CreateTransactionRequestTransaction
                        .builder()
                        .debitAccountName("life-insurance")
                        .creditAccountName(claim.getDeceased().toString())
                        .amount(calculatePayout())
                        .debitRef("Life insurance pay out for death of " + claim.getDeceased())
                        .creditRef("Claim for " + claim.getDeceased() + " paid to " + claim.getNextOfKin())
                        .build()
                )
                .toList();

        return communicationLayer
                .getCommercialBankAPI()
                .createTransactions(new CreateTransactionRequest(transactions));
    }

    private Long calculatePayout() {
        Price currentPremium = priceRepository.findFirstByOrderByInceptionDateDesc();
        return currentPremium.getPrice() * 30;
    }


    public Result<DebitOrderResponseTemplate> createDebitOrder(List<Long> personaIds) {
        Long currentPremiumPrice = priceRepository.findFirstByOrderByInceptionDateDesc().getPrice();

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
                    .build()
            )
            .toList();

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

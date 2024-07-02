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

//    public Result<DebitOrderRequest> updateDebitOrders(List<Long> personaIds, double amount) {
//        List<Result<DebitOrderRequest>> results = personaIds.stream()
//            .map(personaId -> {
//                // build requests
//                DebitOrderRequest claimRequest = DebitOrderRequest
//                        .builder()
//                        .debitAccountName(personaId.toString())
//                        .debitOrderAmount(amount)
//                        .debitOrderReceiverRef("Life Insurance premium from " + personaId)
//                        .debitOrderSenderRef("Life Insurance premium")
//                        .build();
//
//                // send requests
//                return communicationLayer
//                        .getCommercialBankAPI()
//                        .createDebitOrder(claimRequest);
//            })
//            .collect(Collectors.toList());
//
//        // return results;
//        return null;
//    }
}

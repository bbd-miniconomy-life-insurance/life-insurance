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
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.editdebitorder.EditDebitOrderResponse;
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

    public Result<CreateTransactionResponse> createTransactions(List<CreateTransactionRequestTransaction> transactions) {
        return communicationLayer
                .getCommercialBankAPI()
                .createTransactions(new CreateTransactionRequest(transactions));
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

    public Result<EditDebitOrderResponse> updateDebitOrder(String debitOrderReferenceNumber, long amount, long personaId) {
        // create request
        var debitOrderRequests = DebitOrderRequest
                .builder()
                .debitAccountName("life-insurance")
                .creditAccountName(String.valueOf(personaId))
                .amount(amount)
                .debitRef("Life Insurance premium for " + personaId + " activated")
                .creditRef("Life Insurance premium")
                .build();

        // call commercial
        return communicationLayer.getCommercialBankAPI().updateDebitOrder(debitOrderReferenceNumber, debitOrderRequests);
    }
}

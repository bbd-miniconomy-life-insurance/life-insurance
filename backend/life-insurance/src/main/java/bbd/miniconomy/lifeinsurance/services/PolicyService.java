package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.models.entities.DebitOrder;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.DebitOrderRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice.GetPriceResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;

import java.util.List;

@Service
public class PolicyService {
    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;
    private final DebitOrderRepository debitOrderRepository;
    private final HandOfZeusService handOfZeusService;
    private final PriceRepository priceRepository;

    public PolicyService(CommercialBankService commercialBankService, PolicyRepository policyRepository, DebitOrderRepository debitOrderRepository, HandOfZeusService handOfZeusService, PriceRepository priceRepository) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.debitOrderRepository = debitOrderRepository;
        this.handOfZeusService = handOfZeusService;
        this.priceRepository = priceRepository;
    }

    public void activatePolicy(List<Long> personaIds) {

        List<Long> activePersonaIds = personaIds.stream()
                .filter(personaId -> !policyRepository.existsByPersonaId(personaId))
                .peek(personaId -> policyRepository.insertPolicy(personaId, "null"))
                .toList();

        if (activePersonaIds.isEmpty()) {
            return;
        }

        Result<DebitOrderResponseTemplate> debitOrderResponseResult = commercialBankService.createDebitOrder(activePersonaIds);

        if (debitOrderResponseResult.isFailure()) {
            return;
        }

        debitOrderResponseResult
                .getValue()
                .getData()
                .getItems()
                .forEach(
                    response -> debitOrderRepository
                            .insertDebitOrder(Long.valueOf(response.getCreditAccountName()), response.getId())
                );
    }

    // TODO: new function that will call zeus for price
    public void setPolicyPrice() {
        // Result<GetPriceResponseData> getPriceResponseResult = commercialBankService.createDebitOrder(activePersonaIds);
        Result<GetPriceResponse> getPriceResponseResult = handOfZeusService.getPriceOfLifeInsurance();

        if (getPriceResponseResult.isFailure()) {
            // Todo: set price to 0
            return;
        }

        long latestPrice = priceRepository.findFirstByOrderByInceptionDateDesc().getPrice();

        if (latestPrice == getPriceResponseResult.getValue().getPrice()) {
            return;
        }

        Price newPrice = new Price();
        newPrice.setPrice(getPriceResponseResult.getValue().getPrice());
        newPrice.setInceptionDate("todo");
        priceRepository.save(newPrice);

        // Update all active debit orders with Commercial Bank
        policyRepository.findAllByStatus_StatusName(StatusName.Active)
                .forEach(policy -> {
                    DebitOrder debitOrder = debitOrderRepository.findByPolicy(policy);
                    commercialBankService.updateDebitOrder(debitOrder.getDebitOrderReferenceNumber(), getPriceResponseResult.getValue().getPrice(), policy.getPersonaId());
                });


    }

}

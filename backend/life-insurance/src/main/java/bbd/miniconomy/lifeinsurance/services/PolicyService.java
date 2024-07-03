package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.entities.DebitOrder;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.PolicyStatus;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.DebitOrderRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyStatusRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice.GetPriceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {
    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;
    private final DebitOrderRepository debitOrderRepository;
    private final HandOfZeusService handOfZeusService;
    private final PriceRepository priceRepository;
    private final PolicyStatusRepository policyStatusRepository;
    private final TimeService timeService;

    public PolicyService(CommercialBankService commercialBankService, PolicyRepository policyRepository, DebitOrderRepository debitOrderRepository, HandOfZeusService handOfZeusService, PriceRepository priceRepository, PolicyStatusRepository policyStatusRepository, TimeService timeService) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.debitOrderRepository = debitOrderRepository;
        this.handOfZeusService = handOfZeusService;
        this.priceRepository = priceRepository;
        this.policyStatusRepository = policyStatusRepository;
        this.timeService = timeService;
    }

    public void activatePolicy(List<Long> personaIds) {

        PolicyStatus policyStatus = policyStatusRepository.findPolicyStatusByStatusName("Active");

        List<Long> activePersonaIds = personaIds.stream()
                .filter(personaId -> !policyRepository.existsByPersonaId(personaId))
                .peek(personaId -> {
                    Policy policy = new Policy();
                    policy.setPersonaId(personaId);
                    policy.setStatus(policyStatus);
                    policy.setInceptionDate(timeService.getGameTime());

                    policyRepository.save(policy);
                })
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
            return;
        }

        long latestPrice = priceRepository.findFirstByOrderByInceptionDateDesc().getPrice();
        long zeusPrice = getPriceResponseResult.getValue().getPrice();

        if (latestPrice == zeusPrice) {
            return;
        }

        Price newPrice = new Price();
        newPrice.setPrice(zeusPrice);
        newPrice.setInceptionDate(timeService.getGameTime());
        priceRepository.save(newPrice);

        // Update all active debit orders with Commercial Bank
        policyRepository.findAllByStatus_StatusName("Active")
                .forEach(policy -> {
                    DebitOrder debitOrder = debitOrderRepository.findByPolicy(policy);
                    commercialBankService.updateDebitOrder(debitOrder.getDebitOrderReferenceNumber(), zeusPrice, policy.getPersonaId());
                });

    }

}

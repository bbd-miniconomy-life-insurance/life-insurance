package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.repositories.DebitOrderRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {
    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;
    private final DebitOrderRepository debitOrderRepository;

    public PolicyService(CommercialBankService commercialBankService, PolicyRepository policyRepository, DebitOrderRepository debitOrderRepository) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.debitOrderRepository = debitOrderRepository;
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
}

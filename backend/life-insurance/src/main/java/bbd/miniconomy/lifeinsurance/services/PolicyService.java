package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.PolicyInsertDTO;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.DebitOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class PolicyService {
    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository, commercialBankService) {
        this.policyRepository = policyRepository;
        this.commercialBankService = commercialBankService;
    }

    public void activatePolicy(List<Long> personaIds) {
        // TODO get time & don't add to existing
        var activePersonaIds = personaIds.stream()
            .filter(personaId -> !policyRepository.existsByPersonaId())
            .forEach(personaId -> policyRepository.insertPolicy(personaId, "null"))
            .toList();
        
        if (activePersonaIds.isEmpty()) {
            return;
        }

        Result<DebitOrderListResponseTemplate> debitOrderResponse = commercialBankService
                .createDebitOrder();

        if (debitOrderResponse.isFailure()) {
            return;
        }

        debitOrderResponse
            .getData()
            .getItem()
            .stream()
            .forEach(
                response -> debitOrderRepository.insertDebitOrder(Long.Parse(response.creditAccountName), response.responseId);
            )
    }
}

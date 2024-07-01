package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.PolicyInsertDTO;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Transactional
    public void activatePolicy(Long personaId, Date inceptionDate) {
        PolicyInsertDTO policyInsertDTO = new PolicyInsertDTO(personaId, inceptionDate);
        policyRepository.insertPolicy(policyInsertDTO.getPersonaId(), policyInsertDTO.getInceptionDate());
    }

    @Transactional
    public void activatePolicies(List<PolicyInsertDTO> policyInsertDTOs) {
        policyRepository.insertPolicies(policyInsertDTOs);
    }
}

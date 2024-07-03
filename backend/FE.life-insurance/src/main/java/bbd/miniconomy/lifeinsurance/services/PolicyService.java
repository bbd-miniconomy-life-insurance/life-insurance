package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.PolicyDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    @Autowired
    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }


    public  List<PolicyDTO> getAllPoliciesWithNames(int page,int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Policy> policyPage = policyRepository.findAll(pageable);

        return policyPage
                .getContent()
                .stream()
                .map(policy -> PolicyDTO
                        .builder()
                        .id(policy.getId())
                        .personaId(policy.getPersonaId())
                        .status(policy.getStatus().getStatusName())
                        .inceptionDate(policy.getInceptionDate())
                        .build()
                )
                .toList();
    }

}

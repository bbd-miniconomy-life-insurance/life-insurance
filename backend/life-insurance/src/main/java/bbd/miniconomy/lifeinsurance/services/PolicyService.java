import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Transactional
    public void activatePolicy(Long personaId, Date inceptionDate) {
        PolicyInsertDTO policyInsertDTO = new PolicyInsertDTO(personaId, inceptionDate);
        policyRepository.insertPolicy(policyInsertDTO.getPersonaId().intValue(), policyInsertDTO.getInceptionDate());
    }

    @Transactional
    public void activatePolicies(List<PolicyInsertDTO> policyInsertDTOs) {
        policyRepository.insertPolicies(policyInsertDTOs);
    }
}

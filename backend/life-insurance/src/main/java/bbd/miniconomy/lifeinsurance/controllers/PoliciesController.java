package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.PolicyPriceRequestDTO;
import bbd.miniconomy.lifeinsurance.models.dto.PolicyRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;

@RestController
@RequestMapping("/policies")
public class PoliciesController {

    private final PolicyRepository policyRepository;

    public PoliciesController(
            PolicyRepository policyRepository
    ) {
        this.policyRepository = policyRepository;
    }

    @PostMapping("/prices")
    public void UpdatePolicyPrice(@RequestBody PolicyPriceRequestDTO request) {
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

}

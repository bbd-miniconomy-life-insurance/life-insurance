package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.PolicyDTO;
import bbd.miniconomy.lifeinsurance.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;
    @GetMapping
    public ResponseEntity<List<PolicyDTO>> getAllPolicies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PolicyDTO> policies = policyService.getAllPoliciesWithNames(page,size);
        return ResponseEntity.ok(policies);
    }

}


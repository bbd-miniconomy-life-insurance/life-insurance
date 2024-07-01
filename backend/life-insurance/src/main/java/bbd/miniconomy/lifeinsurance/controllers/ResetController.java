package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reset")
public class ResetController {

    private final PolicyRepository policyRepository;

    public ResetController(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @PostMapping
    public ResponseEntity<GlobalLifeInsuranceResponse> ResetLifeInsuranceService() {
        policyRepository.resetDatabase();
        return new ResponseEntity<>(
                GlobalLifeInsuranceResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Life Insurance Successfully Reset")
                        .build(),
                HttpStatus.OK
        );
    }

}
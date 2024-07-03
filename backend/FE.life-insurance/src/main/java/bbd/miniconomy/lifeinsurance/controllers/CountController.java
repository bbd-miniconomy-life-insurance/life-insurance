package bbd.miniconomy.lifeinsurance.controllers;
import bbd.miniconomy.lifeinsurance.services.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bbd.miniconomy.lifeinsurance.models.dto.CountDTO;

@RestController
@RequestMapping("/api/v1/counts")
public class CountController {

    private final CountService countService;

    @Autowired
    public CountController(CountService countService) {
        this.countService = countService;
    }

    @GetMapping
    public CountDTO getCounts() {
        Long transactionCount = countService.countTransactions();
        Long policyCount = countService.countPolicies();
        return CountDTO.builder()
                .transactionCount(transactionCount)
                .policyCount(policyCount)
                .build();
    }
}


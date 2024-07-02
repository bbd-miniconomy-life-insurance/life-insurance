package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.PolicyPriceRequestDTO;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.CommercialBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import bbd.miniconomy.lifeinsurance.enums.StatusName;

import java.time.LocalDate;

@RestController
@RequestMapping("/policies")
public class PolicyPriceController {

    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private CommercialBankService commercialBankService;

    @PostMapping("/prices")
    @ResponseStatus(HttpStatus.OK)
    public void updatePolicyPrice(@RequestBody PolicyPriceRequestDTO request) {
        // Retrieve the latest price and check if the new price is different
        long latestPrice = priceRepository.findFirstByOrderByInceptionDateDesc().getPrice();
        if (latestPrice != request.getPrice()) {
            // Update all active debit orders with Commercial Bank
            policyRepository.findAllByStatus_StatusName(StatusName.Active).forEach(policy -> {
                commercialBankService.updateDebitOrder(policy.getDebitOrderReferenceNumber(), request.getPrice());
            });

            // Insert a new price record in the price table
            priceRepository.insertNewPrice(LocalDate.now(), request.getPrice());

            // Optionally, you can return a success response here
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New price is the same as current price");
        }
    }
}

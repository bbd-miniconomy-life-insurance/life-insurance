package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.models.dto.PolicyPriceRequestDTO;
import bbd.miniconomy.lifeinsurance.repositories.DebitOrderRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.CommercialBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policies")
public class PoliciesController {

    private final PolicyRepository policyRepository;
    private final PriceRepository priceRepository;
    private final CommercialBankService commercialBankService;
    private final DebitOrderRepository debitOrderRepository;

    public PoliciesController(
            PolicyRepository policyRepository,
            PriceRepository priceRepository,
            CommercialBankService commercialBankService, DebitOrderRepository debitOrderRepository
    ) {
        this.policyRepository = policyRepository;
        this.priceRepository = priceRepository;
        this.commercialBankService = commercialBankService;
        this.debitOrderRepository = debitOrderRepository;
    }

    @PostMapping("/prices")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GlobalLifeInsuranceResponse> updatePolicyPrice(@RequestBody PolicyPriceRequestDTO request) {
        // Retrieve the latest price and check if the new price is different
        long latestPrice = priceRepository.findFirstByOrderByInceptionDateDesc().getPrice();

        if (latestPrice == request.getPrice()) {
            return new ResponseEntity<>(
                    GlobalLifeInsuranceResponse
                            .builder()
                            .status(HttpStatus.OK.value())
                            .message("Nothing to update.")
                            .build(),
                    HttpStatus.OK
            );
        }

        // Insert a new price record in the price table
        // TODO: fix the date
        Price newPrice = new Price();
        newPrice.setPrice(request.getPrice());
        newPrice.setInceptionDate("todo");
        priceRepository.save(newPrice);

        // Update all active debit orders with Commercial Bank
        policyRepository.findAllByStatus_StatusName(StatusName.Active)
                .forEach(policy -> {
                    DebitOrder debitOrder = debitOrderRepository.findByPolicy(policy);
                    commercialBankService.updateDebitOrder(debitOrder.getDebitOrderReferenceNumber(), request.getPrice(), policy.getPersonaId());
                });

        return new ResponseEntity<>(
                GlobalLifeInsuranceResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Policy Price Updated Successfully")
                        .build(),
                HttpStatus.OK
        );
    }
}

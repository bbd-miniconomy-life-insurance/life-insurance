package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.ClaimRequestDTO;
import bbd.miniconomy.lifeinsurance.models.dto.Response;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.CommercialBankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/claims")
public class ClaimsController {

    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;
    private final PriceRepository priceRepository;

    public ClaimsController(
            CommercialBankService commercialBankService,
            PolicyRepository policyRepository,
            PriceRepository priceRepository
    ) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.priceRepository = priceRepository;
    }

    @PostMapping
    public ResponseEntity<Response> payClaim(@RequestBody @Valid ClaimRequestDTO request) {
        Policy deceasedPolicy = policyRepository.findByPersonaId(request.getPersonaId());
        if (deceasedPolicy == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Policy for deceased persona does not exist.")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (deceasedPolicy.getStatus().getStatusName().equals("PaidOut")) {
            return new ResponseEntity<>(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Life Insurance has been paid out already.")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        Price currentPremium = priceRepository.findFirstByOrderByInceptionDateDesc();
        double DeathBenefitSum = currentPremium.getPrice() * 30;
        var transactionResult = commercialBankService
                .createTransaction(
                        request.getPersonaId(),
                        request.getNextOfKinId(),
                        DeathBenefitSum
                );

        if (transactionResult.isFailure()) {
            // should technically never be?
            // How will we have any clue what happened to the payment?
            // if our accounts were too low to pay out - buy stocks, try again.
            // Return response of bank if something went wrong (or just say they don't have an account)
        }

       return new ResponseEntity<>(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Successfully paid Death Benefit of " + request.getPersonaId() + " to " + request.getNextOfKinId() + "(next of kin).")
                        .build(),
                HttpStatus.OK
        );
    }
}

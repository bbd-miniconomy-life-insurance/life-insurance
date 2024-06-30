package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.ClaimRequestDTO;
import bbd.miniconomy.lifeinsurance.models.dto.Response;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.CommercialBankService;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        // Validate that policy exists
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

        // Validate policy paid out status
        if (StatusName.PaidOut.equals(deceasedPolicy.getStatus().getStatusName())) {
            return new ResponseEntity<>(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Life Insurance has been paid out already.")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (StatusName.Pending.equals(deceasedPolicy.getStatus().getStatusName())) {
            return new ResponseEntity<>(
                    Response.builder()
                            .status(HttpStatus.ACCEPTED.value())
                            .message("Payment has already been initiated for next of kin persona " + request.getNextOfKinId() + ". Waiting on Feedback from Bank.")
                            .build(),
                    HttpStatus.ACCEPTED
            );
        }

        if (StatusName.Lapsed.equals(deceasedPolicy.getStatus().getStatusName())) {
            return new ResponseEntity<>(
                    Response.builder()
                            .status(HttpStatus.OK.value())
                            .message(request.getPersonaId() + "'s life insurance debit order was in lapsed state for the month. No pay out will be made to " + request.getNextOfKinId())
                            .build(),
                    HttpStatus.OK
            );
        }


        // calculate payout.
        Price currentPremium = priceRepository.findFirstByOrderByInceptionDateDesc();
        Long deathBenefitSum = currentPremium.getPrice() * 30;

        // pay next of kin
        Result<TransactionResponse> transactionResult = commercialBankService
                .createTransaction(
                        request.getPersonaId(),
                        request.getNextOfKinId(),
                        deathBenefitSum
                );

        // would probably never happen: commercial response was empty or has no first record
        if (transactionResult.isFailure()) {
            return new ResponseEntity<>(
                    Response.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Could not determine payment status.")
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        TransactionResponse payment = transactionResult.getValue();

        // react to payment request status
        switch (payment.getStatus()) {
            case pending -> {
                return new ResponseEntity<>(
                        Response.builder()
                                .status(HttpStatus.ACCEPTED.value())
                                .message("Payment has been initiated for next of kin persona " + request.getNextOfKinId() + ". Waiting on Feedback from Bank.")
                                .build(),
                        HttpStatus.ACCEPTED
                );
            }

            case completed -> {
                return new ResponseEntity<>(
                        Response.builder()
                                .status(HttpStatus.OK.value())
                                .message("Successfully paid Death Benefit of " + request.getPersonaId() + " to " + request.getNextOfKinId() + "(next of kin).")
                                .build(),
                        HttpStatus.OK
                );
            }

            default -> {
                // null: Why would there ever be no status?
                return new ResponseEntity<>(
                        Response.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("Could not determine payment status.")
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        }
    }
}

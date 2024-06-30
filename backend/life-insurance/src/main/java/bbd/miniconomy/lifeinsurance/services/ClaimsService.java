package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.DeathDTO;
import bbd.miniconomy.lifeinsurance.models.dto.Response;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.PolicyStatus;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyStatusRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ClaimsService {

    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;
    private final PriceRepository priceRepository;
    private final PolicyStatusRepository policyStatusRepository;

    public ClaimsService(
            CommercialBankService commercialBankService,
            PolicyRepository policyRepository,
            PriceRepository priceRepository, PolicyStatusRepository policyStatusRepository
    ) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.priceRepository = priceRepository;
        this.policyStatusRepository = policyStatusRepository;
    }

    public Response payClaim(DeathDTO claim) {

        Result<Policy> deseasedPolicyResult = getValidPolicy(claim);
        if (deseasedPolicyResult.isFailure()) {
            return Response.builder()
                    .status(deseasedPolicyResult.getErrorCode())
                    .message(deseasedPolicyResult.getErrorMessage())
                    .build();
        }

        Result<TransactionResponse> transactionResult = commercialBankService
                .createTransaction(
                        claim.getDeceased(),
                        claim.getNextOfKin(),
                        calculatePayout()
                );

        if (transactionResult.isFailure()) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Could not determine payment status.")
                    .build();
        }

        Policy deceasedPolicy = deseasedPolicyResult.getValue();
        return updatePolicyBasedOnPaymentStatus(transactionResult.getValue(), claim, deceasedPolicy);
    }

    private Result<Policy> getValidPolicy(DeathDTO claim) {
        Policy deceasedPolicy = policyRepository.findByPersonaId(claim.getDeceased());

        if (deceasedPolicy == null) {
            return Result.failure("Policy for deceased persona does not exist.", HttpStatus.BAD_REQUEST.value());
        }

        switch (deceasedPolicy.getStatus().getStatusName()) {
            case PaidOut -> {
                return Result.failure(
                        "Life Insurance has been paid out already.",
                        HttpStatus.BAD_REQUEST.value());
            }
            case Pending -> {
                return Result.failure(
                        "Payment already initiated to next of kin. Waiting on feedback from bank.",
                        HttpStatus.ACCEPTED.value()
                );
            }
            case Lapsed -> {
                return Result.failure(
                        "Debit order was in a lapsed state when persona died. No claim can be made.",
                        HttpStatus.BAD_REQUEST.value()
                );
            }
            case Active -> {
                return Result.success(deceasedPolicy);
            }
            default -> {
                // should never happen
                return Result.failure(
                        "Could not determine deceased policy status",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                );
            }
        }
    }

    private Response updatePolicyBasedOnPaymentStatus(TransactionResponse payment, DeathDTO claim, Policy deceasedPolicy) {
        switch (payment.getStatus()) {
            case pending -> {
                Result<Policy> updatedPolicyResult = updatePolicyStatus(deceasedPolicy, StatusName.Pending);

                if (updatedPolicyResult.isFailure()) {
                    return Response.builder()
                            .status(updatedPolicyResult.getErrorCode())
                            .message(updatedPolicyResult.getErrorMessage())
                            .build();
                }

                return Response.builder()
                        .status(HttpStatus.ACCEPTED.value())
                        .message("Payment has been initiated for next of kin . Waiting on Feedback from Bank.")
                        .build();
            }

            case completed -> {
                Result<Policy> updatedPolicyResult = updatePolicyStatus(deceasedPolicy, StatusName.Active);

                if (updatedPolicyResult.isFailure()) {
                    return Response.builder()
                            .status(updatedPolicyResult.getErrorCode())
                            .message(updatedPolicyResult.getErrorMessage())
                            .build();
                }

                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Successfully paid claim to next of kin persona.")
                        .build();
            }

            default -> {
                // null: Why would there ever be no status?
                return Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Could not determine payment status.")
                        .build();
            }
        }
    }

    private Result<Policy> updatePolicyStatus(Policy deceasedPolicy, StatusName status) {
        try {
            PolicyStatus completedStatus = policyStatusRepository.findPolicyStatusByStatusName(status);
            deceasedPolicy.setStatus(completedStatus);
            return Result.success(policyRepository.save(deceasedPolicy));
        } catch (Exception e) {
            return Result.failure(
                    "Could not update policy status.",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
        }
    }

    private Long calculatePayout() {
        Price currentPremium = priceRepository.findFirstByOrderByInceptionDateDesc();
        return currentPremium.getPrice() * 30;
    }
}

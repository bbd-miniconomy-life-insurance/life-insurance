package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.lifeevents.LifeEventsDeathDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyStatusRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponsePaymentStatus;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void payClaims(List<LifeEventsDeathDTO> claims) {
        List<LifeEventsDeathDTO> validClaims = claims
                .stream()
                .filter(claim -> policyRepository.existsByPersonaId(claim.getDeceased()))
                .filter(claim -> policyRepository.existsByPersonaIdAndStatus_StatusName(claim.getDeceased(), StatusName.Active))
                .toList();

        List<CreateTransactionRequestTransaction> validTransactions = validClaims
                .stream()
                .map(claim -> CreateTransactionRequestTransaction
                        .builder()
                        .debitAccountName("life-insurance")
                        .creditAccountName(claim.getDeceased().toString())
                        .amount(calculatePayout())
                        .debitRef("Life insurance pay out for death of " + claim.getDeceased())
                        .creditRef("Claim for " + claim.getDeceased() + " paid to " + claim.getNextOfKin())
                        .build()
                )
                .toList();

        if (validTransactions.isEmpty()) {
            return;
        }

        Result<CreateTransactionResponse> transactionsResult = commercialBankService
                .createTransactions(new CreateTransactionRequest(validTransactions));

        if (transactionsResult.isFailure()) {
            return;
        }

        CreateTransactionResponse transactions = transactionsResult.getValue();

        // TODO: Refactor assumption that validClaims and validTransactions are the same length and order
        for (int i = 0; i < validClaims.size(); i++) {
            // for each one, update the policy record
            Policy deceasedPolicy = policyRepository.findByPersonaId(validClaims.get(i).getDeceased());
            CreateTransactionResponsePaymentStatus transactionStatus = CreateTransactionResponsePaymentStatus.valueOf(transactions.getData().getItems().get(i).getStatus());
            switch (transactionStatus) {
                case pending -> {
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName(StatusName.Pending));
                    policyRepository.save(deceasedPolicy);
                }
                case completed -> {
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName(StatusName.PaidOut));
                    policyRepository.save(deceasedPolicy);
                }
                default -> {
                    // TODO: means the transactionStatus is unknown... do nothing for now.
                }
            }
        }
    }

    private Long calculatePayout() {
        Price currentPremium = priceRepository.findFirstByOrderByInceptionDateDesc();
        return currentPremium.getPrice() * 30;
    }
}

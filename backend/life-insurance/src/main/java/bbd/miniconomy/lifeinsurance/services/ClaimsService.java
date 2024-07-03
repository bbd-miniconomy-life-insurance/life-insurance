package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.lifeevents.LifeEventsDeathDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.models.entities.Transaction;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyStatusRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.repositories.TransactionRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponsePaymentStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimsService {

    private final CommercialBankService commercialBankService;
    private final PolicyRepository policyRepository;
    private final PolicyStatusRepository policyStatusRepository;
    private final TransactionRepository transactionRepository;
    private final PriceRepository priceRepository;

    public ClaimsService(
            CommercialBankService commercialBankService,
            PolicyRepository policyRepository,
            PolicyStatusRepository policyStatusRepository,
            TransactionRepository transactionRepository, PriceRepository priceRepository
    ) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.transactionRepository = transactionRepository;
        this.policyStatusRepository = policyStatusRepository;
        this.priceRepository = priceRepository;
    }

    public void payClaims(List<LifeEventsDeathDTO> claims) {
        List<LifeEventsDeathDTO> validClaims = claims
                .stream()
                .filter(claim -> policyRepository.existsByPersonaId(claim.getDeceased()))
                .filter(claim -> policyRepository.existsByPersonaIdAndStatus_StatusName(claim.getDeceased(), "Active"))
                .toList();

        if (validClaims.isEmpty()) {
            return;
        }

        var validTransactions = claims
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

        Result<CreateTransactionResponse> transactionsResult = commercialBankService.createTransactions(validTransactions);

        if (transactionsResult.isFailure()) {
            return;
        }

        CreateTransactionResponse transactions = transactionsResult.getValue();

        // TODO: Refactor assumption that validClaims and validTransactions are the same length and order
        // Could use the credit ref message: Claim for " + claim.getDeceased() + " paid to " + claim.getNextOfKin()
        for (int i = 0; i < validClaims.size(); i++) {
            // for each one, update the policy record
            Policy deceasedPolicy = policyRepository.findByPersonaId(validClaims.get(i).getDeceased());
            var responseFromBank = transactions.getData().getItems().get(i);

            // save transaction
            Transaction transaction = new Transaction();
            transaction.setPolicy(deceasedPolicy);
            transaction.setTransactionReferenceNumber(responseFromBank.getId());
            transactionRepository.save(transaction);

            // update policy
            CreateTransactionResponsePaymentStatus transactionStatus = CreateTransactionResponsePaymentStatus.valueOf(responseFromBank.getStatus());
            switch (transactionStatus) {
                case pending -> {
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName("Pending"));
                    policyRepository.save(deceasedPolicy);
                }
                case completed -> {
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName("PaidOut"));
                    policyRepository.save(deceasedPolicy);
                }
                default -> {
                    // This is actually an issue...
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName("Unknown"));
                    policyRepository.save(deceasedPolicy);
                }
            }
        }
    }

    private Long calculatePayout() {
        Price currentPremium = priceRepository.findFirstByOrderByInceptionDateDesc();
        return currentPremium.getPrice() * 30;
    }
}

package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.lifeevents.LifeEventsDeathDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.models.entities.Transaction;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PolicyStatusRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.repositories.TransactionRepository;
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
    private final PolicyStatusRepository policyStatusRepository;
    private final TransactionRepository transactionRepository;

    public ClaimsService(
            CommercialBankService commercialBankService,
            PolicyRepository policyRepository,
            PolicyStatusRepository policyStatusRepository,
            TransactionRepository transactionRepository
    ) {
        this.commercialBankService = commercialBankService;
        this.policyRepository = policyRepository;
        this.transactionRepository = transactionRepository;
        this.policyStatusRepository = policyStatusRepository;
    }

    public void payClaims(List<LifeEventsDeathDTO> claims) {
        List<LifeEventsDeathDTO> validClaims = claims
                .stream()
                .filter(claim -> policyRepository.existsByPersonaId(claim.getDeceased()))
                .filter(claim -> policyRepository.existsByPersonaIdAndStatus_StatusName(claim.getDeceased(), StatusName.Active))
                .toList();

        if (validClaims.isEmpty()) {
            return;
        }

        Result<CreateTransactionResponse> transactionsResult = commercialBankService.createTransactions(validClaims);

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
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName(StatusName.Pending));
                    policyRepository.save(deceasedPolicy);
                }
                case completed -> {
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName(StatusName.PaidOut));
                    policyRepository.save(deceasedPolicy);
                }
                default -> {
                    // This is actually an issue...
                    deceasedPolicy.setStatus(policyStatusRepository.findPolicyStatusByStatusName(StatusName.Unknown));
                    policyRepository.save(deceasedPolicy);
                }
            }
        }
    }
}

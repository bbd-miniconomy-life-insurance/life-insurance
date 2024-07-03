package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final PolicyRepository policyRepository;

    @Autowired
    public CountService(TransactionHistoryRepository transactionHistoryRepository, PolicyRepository policyRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.policyRepository = policyRepository;
    }

    public Long countTransactions() {
        return transactionHistoryRepository.count();
    }

    public Long countPolicies() {
        return policyRepository.count();
    }
}


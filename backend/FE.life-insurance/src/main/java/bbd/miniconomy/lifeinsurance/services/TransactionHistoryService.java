package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.TransactionHistoryDTO;
import bbd.miniconomy.lifeinsurance.models.entities.TransactionHistory;
import bbd.miniconomy.lifeinsurance.repositories.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository){this.transactionHistoryRepository = transactionHistoryRepository;}

    public List<TransactionHistoryDTO> getTransactionHistory(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionHistory> historyPage = transactionHistoryRepository.findAll(pageable);
        return historyPage
                .getContent()
                .stream()
                .map(transaction -> TransactionHistoryDTO
                        .builder()
                        .id(transaction.getId())
                        .amount(transaction.getAmount())
                        .reference(transaction.getReference())
                        .date(transaction.getDate())
                        .build()
                ).toList();
    }

}

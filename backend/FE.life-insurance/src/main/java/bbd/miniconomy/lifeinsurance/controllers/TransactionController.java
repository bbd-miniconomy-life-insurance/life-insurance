package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.TransactionHistoryDTO;
import bbd.miniconomy.lifeinsurance.models.entities.TransactionHistory;
import bbd.miniconomy.lifeinsurance.services.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    TransactionHistoryService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionHistoryDTO>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<TransactionHistoryDTO> transactions = transactionService.getTransactionHistory(page, size);
        return ResponseEntity.ok(transactions);
    }
}

package bbd.miniconomy.lifeinsurance.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bbd.miniconomy.lifeinsurance.models.entities.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer>{
    @Query("SELECT SUM(transactionHistory.amount) FROM TransactionHistory transactionHistory WHERE transactionHistory.date BETWEEN :start AND :end")
    Long findSumOfTransactionsFromLastMonth(LocalDateTime start, LocalDateTime end);
}

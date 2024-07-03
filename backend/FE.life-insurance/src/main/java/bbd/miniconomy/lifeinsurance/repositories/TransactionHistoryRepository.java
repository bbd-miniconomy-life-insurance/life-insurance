package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,Long> {

    Page<TransactionHistory> findAll(Pageable pageable);
}
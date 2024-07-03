package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.DebitOrder;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DebitOrderRepository extends JpaRepository<DebitOrder, Long> {
    @Modifying
    @Transactional
    @Query(value = "CALL insert_debit_order(:personaId, :debitOrderReference)", nativeQuery = true)
    void insertDebitOrder(@Param("personaId") Long personaId, @Param("debitOrderReference") String debitOrderReference);

    DebitOrder findByPolicy(Policy policy);
}

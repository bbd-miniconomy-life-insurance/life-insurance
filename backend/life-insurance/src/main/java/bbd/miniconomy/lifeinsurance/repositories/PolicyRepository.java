package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    boolean existsByPersonaId(Long personaId);

    boolean existsByPersonaIdAndStatus_StatusName(Long personaId, StatusName statusName);

    Policy findByPersonaId(Long personaId);

    @Modifying
    @Transactional
    @Query(value = "CALL insert_policy(:personaId, :inceptionDate)", nativeQuery = true)
    void insertPolicy(@Param("personaId") Long personaId, @Param("inceptionDate") String inceptionDate);

    Long countByStatus_StatusName(StatusName statusName);
    // Adding a method to fetch all policies by their status
    // TODO: Add a query method to get all active policies
    List<Policy> findAllByStatus_StatusName(StatusName statusName);

//    @Query("SELECT COUNT(p) FROM Policy p WHERE p.status.statusName = 'Active'")
//    long countActivePolicies();

}

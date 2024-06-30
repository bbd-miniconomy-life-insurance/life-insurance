package bbd.miniconomy.lifeinsurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Policy findByPersonaId(Long personaId);

    @Modifying
    @Transactional
    @Query(value = "CALL insert_policy(:personaId, :inceptionDate)", nativeQuery = true)
    void insertPolicy(@Param("personaId") Long personaId, @Param("inceptionDate") Date inceptionDate);

    @Modifying
    @Transactional
    default void insertPolicies(List<PolicyInsertDTO> policyInsertDTOs) {
        policyInsertDTOs.stream()
                .forEach(dto -> insertPolicy(dto.getPersonaId().intValue(), dto.getInceptionDate()));
    }

    @Query("SELECT COUNT(p) FROM Policy p WHERE p.status.statusName = 'Active'")
    long countActivePolicies();
}

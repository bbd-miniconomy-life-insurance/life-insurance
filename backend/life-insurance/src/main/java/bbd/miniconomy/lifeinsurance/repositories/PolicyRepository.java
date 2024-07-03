package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    boolean existsByPersonaId(Long personaId);

    boolean existsByPersonaIdAndStatus_StatusName(Long personaId, String statusName);

    Policy findByPersonaId(Long personaId);

//    @Modifying
//    @Transactional
//    @Query(value = "CALL insert_policy(:personaId, :inceptionDate)", nativeQuery = true)
//    void insertPolicy(@Param("personaId") Long personaId, @Param("inceptionDate") String inceptionDate);


    Long countByStatus_StatusName(String statusName);
    // Adding a method to fetch all policies by their status
    // TODO: Add a query method to get all active policies
    List<Policy> findAllByStatus_StatusName(String statusName);

}

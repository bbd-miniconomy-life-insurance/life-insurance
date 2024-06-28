package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    Policy findByPersonaId(Long personaId);

}

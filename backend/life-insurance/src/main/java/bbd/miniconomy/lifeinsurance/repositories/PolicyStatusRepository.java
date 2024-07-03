package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyStatusRepository extends JpaRepository<PolicyStatus, Integer> {
    PolicyStatus findPolicyStatusByStatusName(String statusName);
}

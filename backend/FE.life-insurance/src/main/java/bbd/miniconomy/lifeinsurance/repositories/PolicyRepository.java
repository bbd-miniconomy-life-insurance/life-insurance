package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    Page<Policy> findAll(Pageable pageable);
}

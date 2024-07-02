package bbd.miniconomy.lifeinsurance.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class ResetRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean resetDatabase() {
        return (boolean) entityManager.createNativeQuery("SELECT reset_database()")
                .getSingleResult();
    }


}

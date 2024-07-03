package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstantsRepository extends JpaRepository<Constant, Integer>{
    Constant findByName(String name);
}

package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstantsRepository extends JpaRepository<Constant, Integer>{
    String findIdByName(String name);
}

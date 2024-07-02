package bbd.miniconomy.lifeinsurance.repositories;

import bbd.miniconomy.lifeinsurance.models.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    Price findFirstByOrderByInceptionDateDesc();

    @Modifying
    @Transactional
    @Query("insert into Price (inception_date, price) values (?1, ?2)")
    void insertNewPrice(LocalDate date, long price);
}

package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_id_gen")
    @SequenceGenerator(name = "price_id_gen", sequenceName = "price_price_id_seq", allocationSize = 1)
    @Column(name = "price_id", nullable = false)
    private Integer id;

    @Column(name = "inception_date", nullable = false)
    private Instant inceptionDate;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private Double price;
}
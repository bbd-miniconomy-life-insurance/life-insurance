package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_id_gen")
    @SequenceGenerator(name = "price_id_gen", sequenceName = "price_price_id_seq", allocationSize = 1)
    @Column(name = "price_id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "inception_date", nullable = false, length = 10)
    private String inceptionDate;

    @NotNull
    @Column(name = "price", nullable = false)
    private Long price;

}
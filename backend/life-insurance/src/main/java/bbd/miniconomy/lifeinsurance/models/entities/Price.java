package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "price")
public class Price {
    @Id
    @ColumnDefault("nextval('price_price_id_seq'::regclass)")
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
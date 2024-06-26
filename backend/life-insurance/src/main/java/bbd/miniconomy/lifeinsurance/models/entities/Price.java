package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

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
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Instant inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
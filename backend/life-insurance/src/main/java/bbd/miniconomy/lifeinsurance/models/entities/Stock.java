package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stocks_id_gen")
    @SequenceGenerator(name = "stocks_id_gen", sequenceName = "stocks_stocks_id_seq", allocationSize = 1)
    @Column(name = "stocks_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "\"businessId\"", nullable = false, length = 100)
    private String businessId;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}
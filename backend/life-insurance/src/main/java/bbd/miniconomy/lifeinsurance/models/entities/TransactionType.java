package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_type")
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_type_id_gen")
    @SequenceGenerator(name = "transaction_type_id_gen", sequenceName = "transaction_type_transaction_type_id_seq", allocationSize = 1)
    @Column(name = "transaction_type_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

}
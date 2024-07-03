package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "debit_order")
public class DebitOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "debit_order_id_gen")
    @SequenceGenerator(name = "debit_order_id_gen", sequenceName = "debit_order_debit_order_id_seq", allocationSize = 1)
    @Column(name = "debit_order_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Size(max = 100)
    @NotNull
    @Column(name = "debit_order_reference_number", nullable = false, length = 100)
    private String debitOrderReferenceNumber;

}
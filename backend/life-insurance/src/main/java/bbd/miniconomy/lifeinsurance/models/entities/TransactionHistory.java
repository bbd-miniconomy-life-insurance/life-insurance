package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_history")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_history_id_gen")
    @SequenceGenerator(name = "transaction_history_id_gen", sequenceName = "transaction_history_transaction_history_id_seq", allocationSize = 1)
    @Column(name = "transaction_history_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Size(max = 10)
    @NotNull
    @Column(name = "date", nullable = false, length = 10)
    private String date;

}
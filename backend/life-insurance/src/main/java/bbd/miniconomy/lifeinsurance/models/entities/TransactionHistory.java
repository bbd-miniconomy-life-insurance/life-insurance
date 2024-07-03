package bbd.miniconomy.lifeinsurance.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "transaction_history")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_history_id_gen")
    @SequenceGenerator(name = "transaction_history_id_gen", sequenceName = "transaction_history_transaction_history_id_seq", allocationSize = 1)
    @Column(name = "transaction_history_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Size(max = 10)
    @NotNull
    @Column(name = "date", nullable = false, length = 10)
    private LocalDateTime date;

}
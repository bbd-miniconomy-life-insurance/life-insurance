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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_history_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @NotNull
    @Size(max = 10)
    @Column(name = "date", nullable = false, length = 10)
    private String date;

    @NotNull
    @Size(max = 100)
    @Column(name = "reference", nullable = false, length = 100)
    private String reference;
}

package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @ColumnDefault("nextval('transaction_transaction_id_seq'::regclass)")
    @Column(name = "transaction_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Size(max = 100)
    @NotNull
    @Column(name = "transaction_reference_number", nullable = false, length = 100)
    private String transactionReferenceNumber;

}
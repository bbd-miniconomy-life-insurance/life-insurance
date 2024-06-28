package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_history_id_gen")
    @SequenceGenerator(name = "payment_history_id_gen", sequenceName = "payment_history_payment_history_id_seq", allocationSize = 1)
    @Column(name = "payment_history_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "time", nullable = false)
    private Instant time;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;
}
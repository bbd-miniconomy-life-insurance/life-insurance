package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "policy")
@Getter
@Setter
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policy_id_gen")
    @SequenceGenerator(name = "policy_id_gen", sequenceName = "policy_policy_id_seq", allocationSize = 1)
    @Column(name = "policy_id", nullable = false)
    private Integer id;

    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private PolicyStatus status;

    @Column(name = "inception_date", nullable = false)
    private Instant inceptionDate;

    @OneToMany(mappedBy = "policy")
    private Set<PaymentHistory> paymentHistories = new LinkedHashSet<>();

}
package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "policy")
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public Instant getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Instant inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public Set<PaymentHistory> getPaymentHistories() {
        return paymentHistories;
    }

    public void setPaymentHistories(Set<PaymentHistory> paymentHistories) {
        this.paymentHistories = paymentHistories;
    }

}
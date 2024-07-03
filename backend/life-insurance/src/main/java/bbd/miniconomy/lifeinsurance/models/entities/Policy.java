package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "policy")
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policy_id_gen")
    @SequenceGenerator(name = "policy_id_gen", sequenceName = "policy_policy_id_seq", allocationSize = 1)
    @Column(name = "policy_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private PolicyStatus status;

    @Size(max = 10)
    @NotNull
    @Column(name = "inception_date", nullable = false, length = 10)
    private String inceptionDate;

}
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
@Table(name = "policy")
public class Policy {
    @Id
    @ColumnDefault("nextval('policy_policy_id_seq'::regclass)")
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
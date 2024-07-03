package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "policy_status")
public class PolicyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policy_status_id_gen")
    @SequenceGenerator(name = "policy_status_id_gen", sequenceName = "policy_status_status_id_seq", allocationSize = 1)
    @Column(name = "status_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "status_name", nullable = false, length = 100)
    private String statusName;

}
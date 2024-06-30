package bbd.miniconomy.lifeinsurance.models.entities;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "policy_status")
@Getter
@Setter
public class PolicyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policy_status_id_gen")
    @SequenceGenerator(name = "policy_status_id_gen", sequenceName = "policy_status_status_id_seq", allocationSize = 1)
    @Column(name = "status_id", nullable = false)
    private Integer id;

    @Column(name = "status_name", nullable = false, length = 100)
    private StatusName statusName;
}
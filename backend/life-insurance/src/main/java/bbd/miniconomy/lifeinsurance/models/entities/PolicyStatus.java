package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "policy_status")
public class PolicyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "policy_status_id_gen")
    @SequenceGenerator(name = "policy_status_id_gen", sequenceName = "policy_status_status_id_seq", allocationSize = 1)
    @Column(name = "status_id", nullable = false)
    private Integer id;

    @Column(name = "status_name", nullable = false, length = 100)
    private String statusName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
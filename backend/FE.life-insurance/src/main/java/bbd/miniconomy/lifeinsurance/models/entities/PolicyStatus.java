package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "policy_status")
public class PolicyStatus {
    @Id
    @ColumnDefault("nextval('policy_status_status_id_seq'::regclass)")
    @Column(name = "status_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "status_name", nullable = false, length = 100)
    private String statusName;

}
package bbd.miniconomy.lifeinsurance.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "constants")
@NoArgsConstructor
@AllArgsConstructor
public class Constant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "constants_id_gen")
    @SequenceGenerator(name = "constants_id_gen", sequenceName = "constants_constants_id_seq", allocationSize = 1)
    @Column(name = "constants_id", nullable = false)
    private Integer constantsId;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @NotNull
    @Column(name = "id", nullable = false, length = 100)
    private String id;

}
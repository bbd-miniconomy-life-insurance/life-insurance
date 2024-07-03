package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyInsertDTO {
    private Long personaId;
    private String inceptionDate;
}

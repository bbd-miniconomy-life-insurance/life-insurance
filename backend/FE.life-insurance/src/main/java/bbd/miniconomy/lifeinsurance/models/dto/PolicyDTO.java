package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PolicyDTO {
    private Integer id;
    private Long personaId;
    private String status;
    private String inceptionDate;

}

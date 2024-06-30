package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeathDTO {
    private Long deceased;
    private Long nextOfKin;
}

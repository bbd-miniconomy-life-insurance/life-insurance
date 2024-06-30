package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeEventsDeathDTO {
    private Long deceased;
    private Long nextOfKin;
}

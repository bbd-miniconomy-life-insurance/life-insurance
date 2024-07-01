package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeEventsChildrenDTO {
    private Long parent;
    private Long child;
}

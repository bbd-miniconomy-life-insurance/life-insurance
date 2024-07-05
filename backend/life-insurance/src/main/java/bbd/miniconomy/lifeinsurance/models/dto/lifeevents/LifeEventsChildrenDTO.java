package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class LifeEventsChildrenDTO {
    private Long parent;
    private Long child;
}

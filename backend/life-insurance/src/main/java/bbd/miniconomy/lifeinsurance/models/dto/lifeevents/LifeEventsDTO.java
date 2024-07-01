package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LifeEventsDTO {
    private List<LifeEventsMarriagesDTO> marriages;
    private List<LifeEventsChildrenDTO> children;
    private List<Long> adults;
    private List<LifeEventsDeathDTO> deaths;
}

package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LifeEventsDTO {
    @JsonProperty
    private List<LifeEventsMarriagesDTO> marriages;
    @JsonProperty
    private List<LifeEventsChildrenDTO> children;
    @JsonProperty
    private List<Long> adults;
    @JsonProperty
    private List<LifeEventsDeathDTO> deaths;
}

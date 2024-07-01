package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeEventsDeathDTO {
    private Long deceased;
    @JsonProperty("next_of_kin")
    private Long nextOfKin;
}

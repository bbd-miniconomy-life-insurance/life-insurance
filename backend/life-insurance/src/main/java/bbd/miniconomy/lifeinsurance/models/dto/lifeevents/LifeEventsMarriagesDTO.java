package bbd.miniconomy.lifeinsurance.models.dto.lifeevents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeEventsMarriagesDTO {
    @JsonProperty("partner_a")
    private Long partnerA;

    @JsonProperty("partner_b")
    private Long partnerB;
}

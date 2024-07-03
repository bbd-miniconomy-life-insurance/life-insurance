package bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPriceResponse {
    @JsonProperty("value")
    private Long price;
}

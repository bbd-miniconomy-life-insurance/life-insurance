package bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.gettime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StartTimeResponse {
    @JsonProperty("start_date")
    private LocalDateTime startDate;
}

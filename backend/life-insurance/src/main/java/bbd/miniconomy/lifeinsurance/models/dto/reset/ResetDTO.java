package bbd.miniconomy.lifeinsurance.models.dto.reset;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ResetDTO {
    private String action;
    private LocalDateTime startTime;
}

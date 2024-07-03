package bbd.miniconomy.lifeinsurance.models.dto.reset;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResetDTO {
    private String action;
    private LocalDateTime startTime;
}

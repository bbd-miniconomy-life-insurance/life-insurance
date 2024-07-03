package bbd.miniconomy.lifeinsurance.models.dto.reset;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResetDTO {
    private String action;
    private Date startTime;
}

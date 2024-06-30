package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LifeEventsDTO {
    private List<Object> married; // TODO: ignore for now, but fix later
    private List<Object> children; // TODO: ignore for now, but fix later
    private List<Long> adults;
    private List<DeathDTO> deaths;
}

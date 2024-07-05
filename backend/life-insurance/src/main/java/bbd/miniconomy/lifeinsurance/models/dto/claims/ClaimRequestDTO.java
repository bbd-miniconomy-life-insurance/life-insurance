package bbd.miniconomy.lifeinsurance.models.dto.claims;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ClaimRequestDTO {
    @Min(1)
    @NotNull
    @NotEmpty
    private Long personaId;
    @Min(1)
    @NotNull
    @NotEmpty
    private Long nextOfKinId;
}

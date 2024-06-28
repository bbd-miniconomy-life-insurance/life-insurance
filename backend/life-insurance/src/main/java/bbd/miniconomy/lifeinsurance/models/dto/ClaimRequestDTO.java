package bbd.miniconomy.lifeinsurance.models.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
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

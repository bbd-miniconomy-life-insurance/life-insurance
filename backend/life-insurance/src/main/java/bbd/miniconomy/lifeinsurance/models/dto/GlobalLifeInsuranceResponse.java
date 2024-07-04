package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GlobalLifeInsuranceResponse {
    private int status;
    private String message;

    public GlobalLifeInsuranceResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
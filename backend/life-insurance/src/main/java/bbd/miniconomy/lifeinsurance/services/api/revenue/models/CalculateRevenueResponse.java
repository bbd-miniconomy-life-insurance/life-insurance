package bbd.miniconomy.lifeinsurance.services.api.revenue.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CalculateRevenueResponse {
    private Double taxableAmount;
    private Double taxAmount;
}

package bbd.miniconomy.lifeinsurance.services.api.stockexchange.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockListingResponse {
    private String businessId;
    private Long currentMarketValue;
    private Integer quantity;
}

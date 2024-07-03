package bbd.miniconomy.lifeinsurance.services.api.stockexchange.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SellStockResponse {
    private String ownerId;
    private String businessId;
    private Integer quantity;
    private Long currentMarketValue;
}

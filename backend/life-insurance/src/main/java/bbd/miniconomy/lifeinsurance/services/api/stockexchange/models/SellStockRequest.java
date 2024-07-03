package bbd.miniconomy.lifeinsurance.services.api.stockexchange.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SellStockRequest {
    private String sellerId;
    private String companyId;
    private Integer quantity;
}

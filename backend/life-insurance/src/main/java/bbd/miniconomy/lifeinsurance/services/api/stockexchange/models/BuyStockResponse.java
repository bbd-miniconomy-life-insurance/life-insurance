package bbd.miniconomy.lifeinsurance.services.api.stockexchange.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BuyStockResponse {
    private String referenceId;
    private Long amountToPay;
    private Integer quantity;
}

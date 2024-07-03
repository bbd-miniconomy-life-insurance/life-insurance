package bbd.miniconomy.lifeinsurance.services.api.stockexchange.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateBusinessResponse {
    private String id;
    private String name;
    private String bankAccount;
    private String tradingId;
}

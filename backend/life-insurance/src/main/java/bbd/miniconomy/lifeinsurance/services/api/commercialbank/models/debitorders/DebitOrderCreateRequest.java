package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DebitOrderCreateRequest {
    List<DebitOrderRequest> debitOrders;
}


package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DebitOrderCreateRequest {
    List<DebitOrderRequest> debitOrders;
}


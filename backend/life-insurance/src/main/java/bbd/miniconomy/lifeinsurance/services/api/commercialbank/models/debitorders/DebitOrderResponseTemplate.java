package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DebitOrderResponseTemplate {
    int status;
    DebitOrderListResponseTemplate data;
    String message;
}


package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DebitOrderRequest {
    private final String creditAccountName = "life-insurance";
    private String debitAccountName;
    private double debitOrderAmount;
    private String debitOrderReceiverRef;
    private String debitOrderSenderRef;
}


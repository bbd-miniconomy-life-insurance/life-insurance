package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionRequest {
    private String debitAccountName;
    private double transactionAmount;
    private String debitRef;
    private String creditRef;
    private final String creditAccountName = CommercialBankAPI.BANK_ACCOUNT_NUMBER;
}

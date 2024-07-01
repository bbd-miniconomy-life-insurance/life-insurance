package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommercialBankGlobalResponseError {
    private Integer status;
    private String data;
    private String message;
}

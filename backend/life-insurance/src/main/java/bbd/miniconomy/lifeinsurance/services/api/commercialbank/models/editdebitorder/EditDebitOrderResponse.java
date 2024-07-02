package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.editdebitorder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDebitOrderResponse {
    private int status;
    private EditDebitOrderData data;
    private String message;

}

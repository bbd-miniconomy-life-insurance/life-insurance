package bbd.miniconomy.lifeinsurance.services.api.commercialbank;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.utils.APIHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class CommercialBankAPI {

    private final APIHandler apiHandler;

    private final String BASE_URL = "https://864c6438-0dd5-49f6-8d01-08cdd8b07c5c.mock.pstmn.io";

    CommercialBankAPI(@Autowired APIHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        ResponseEntity<TransactionResponse> response = apiHandler.callAPIOnce(
                BASE_URL + "/transactions/create",
                HttpMethod.POST,
                null,
                request,
                TransactionResponse.class
        );

        return response.getBody();
    }

}

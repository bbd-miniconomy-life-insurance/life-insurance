package bbd.miniconomy.lifeinsurance.services.api.commercialbank;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.DebitOrderRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class CommercialBankAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.commercialbank.projects.bbdgrad.com")
            .defaultHeader("Authorization", "Bearer MY_SECRET_TOKEN")
            .build();

    public CreateTransactionResponse createTransactions(CreateTransactionRequest requests) {
        return client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactions/create")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requests))
                .retrieve()
                .bodyToMono(CreateTransactionResponse.class)
                .block();
    }

    public Result<CreateTransactionResponse> createDebitOrder(DebitOrderRequest claimRequest) {
        return null;
    }
}
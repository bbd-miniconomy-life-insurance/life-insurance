package bbd.miniconomy.lifeinsurance.services.api.commercialbank;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.TransactionResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;


@Component
public class CommercialBankAPI {

    public static final String BANK_ACCOUNT_NUMBER = "life-insurance";

    private static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.commercialbank.projects.bbdgrad.com")
            .defaultHeader("Authorization", "Bearer MY_SECRET_TOKEN")
            .build();

    public Result<TransactionResponse> createTransaction(TransactionRequest body) {
        var transactions = client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/transactions/create")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Collections.singletonList(body)))
                .retrieve()
                .bodyToFlux(TransactionResponse.class)
                .collectList()
                .blockOptional();

        return transactions
                .stream()
                .findFirst()
                .map(List::getFirst)
                .map(Result::success)
                .orElse(Result.failure("Payment Failed."));
    }
}

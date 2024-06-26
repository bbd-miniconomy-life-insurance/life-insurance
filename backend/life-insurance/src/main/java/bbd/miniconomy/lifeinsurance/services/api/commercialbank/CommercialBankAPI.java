package bbd.miniconomy.lifeinsurance.services.api.commercialbank;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderCreateRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderListResponseTemplate;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class CommercialBankAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.commercialbank.projects.bbdgrad.com/")
            .defaultHeader("Authorization", "Bearer MY_SECRET_TOKEN")
            .build();

    public Result<CreateTransactionResponse> createTransactions(CreateTransactionRequest requests) {
        try {
            return Result.success(
                    client
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
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Commercial Bank Failed");
        }
    }

    public Result<DebitOrderResponseTemplate> createDebitOrder(DebitOrderCreateRequest request) {
        try {
            return Result.success(
                    client
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/debitOrders/create")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(request))
                            .retrieve()
                            .bodyToMono(DebitOrderResponseTemplate.class)
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Commercial Bank Failed");
        }
    }
}
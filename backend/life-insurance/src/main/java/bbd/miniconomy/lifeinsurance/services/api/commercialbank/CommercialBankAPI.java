package bbd.miniconomy.lifeinsurance.services.api.commercialbank;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderCreateRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders.DebitOrderResponseTemplate;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.editdebitorder.EditDebitOrderResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;


@Service
public class CommercialBankAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.commercialbank.projects.bbdgrad.com")
            .defaultHeader("X-origin", "life_insurance")
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
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                            .block()
            );
        } catch (Exception e) {
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
                             .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                             .block()
             );
         } catch (Exception e) {
             e.printStackTrace();
             return Result.failure("Communication With Commercial Bank Failed");
         }
     }

    public Result<EditDebitOrderResponse> updateDebitOrder(String debitOrderReferenceNumber, DebitOrderRequest request) {
        try {
            return Result.success(
                    client.put()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/debitOrders/{id}")
                                    .build(debitOrderReferenceNumber))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(request))
                            .retrieve()
                            .bodyToMono(EditDebitOrderResponse.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                            .block()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("Communication With Commercial Bank Failed");
        }
    }
}
package bbd.miniconomy.lifeinsurance.services.api.stockexchange;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequest;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.CreateBusinessRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.CreateBusinessResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.SellStockRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.SellStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.StockListingResponse;

import java.time.Duration;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Component
public class StockExchangeAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://mese.projects.bbdgrad.com")
            .build();

    public void registerBusiness(CreateBusinessRequest requests) {
        try {
            client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/businesses")
                        .queryParam("callbackUrl ", "/register-callback")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requests))
                .retrieve()
                .bodyToMono(CreateBusinessResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .block();
        } catch (Exception e) {
            return;
        }
    }

    public Result<SellStockResponse> sellStocks(SellStockRequest requests) {
        try {
            return Result.success(
                    client
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/stocks/sell")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(requests))
                            .retrieve()
                            .bodyToMono(SellStockResponse.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                            .block()
            );
        } catch (Exception e) {
            return Result.failure("Communication With Stock Exchange Failed");
        }
    }
    
    public void buyStocks(BuyStockRequest requests) {
        try {
            client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/stocks/buy")
                        .queryParam("callbackUrl", "https://api.life.projects.bbdgrad.com/stock/buy-callback")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requests))
                .retrieve()
                .bodyToMono(BuyStockResponse.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .block();
        } catch (Exception e) {
            return;
        }
    }

    public Result<List<StockListingResponse>> stockListing() {
        try {
            return Result.success(
                    client
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/stocks")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToFlux(StockListingResponse.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                            .collectList()
                            .block()
            );
        } catch (Exception e) {
            return Result.failure("Communication With Stock Exchange Failed");
        }
    }
 
    public Result<List<StockListingResponse>> GetAllStocks() {
        try {
            return Result.success(
                    client
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/stocks")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToFlux(StockListingResponse.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                            .collectList()
                            .block()
            );
        } catch (Exception e) {
            return Result.failure("Communication With Stock Exchange Failed");
        }
    }

    public Result<DividendsResponse> Dividends(DividendsRequest requests) {
        try {
            return Result.success(
                    client
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/dividends")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(requests))
                            .retrieve()
                            .bodyToMono(DividendsResponse.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                            .block()
            );
        } catch (Exception e) {
            return Result.failure("Communication With Stock Exchange Failed");
        }
    }
}
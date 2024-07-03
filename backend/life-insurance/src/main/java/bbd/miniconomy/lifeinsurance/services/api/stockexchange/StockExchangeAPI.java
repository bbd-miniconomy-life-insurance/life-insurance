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

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StockExchangeAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://mese.projects.bbdgrad.com/")
            .defaultHeader("Authorization", "Bearer MY_SECRET_TOKEN")
            .build();

    public Result<CreateBusinessResponse> registerBusiness(CreateBusinessRequest requests) {
        try {
            return Result.success(
                    client
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/businesses")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(requests))
                            .retrieve()
                            .bodyToMono(CreateBusinessResponse.class)
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Stock Exchange Failed");
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
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Stock Exchange Failed");
        }
    }
    
    public Result<BuyStockResponse> buyStocks(BuyStockRequest requests) {
        try {
            return Result.success(
                    client
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/stocks/buy")
                                    .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(requests))
                            .retrieve()
                            .bodyToMono(BuyStockResponse.class)
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Stock Exchange Failed");
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
                            .collectList()
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
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
                            .collectList()
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
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
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Stock Exchange Failed");
        }
    }
}
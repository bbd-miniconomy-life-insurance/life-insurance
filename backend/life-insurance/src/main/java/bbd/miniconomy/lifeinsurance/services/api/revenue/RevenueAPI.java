package bbd.miniconomy.lifeinsurance.services.api.revenue;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CreateRevenueRequest;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CreateRevenueResponse;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CalculateRevenueResponse;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CalculateRevenueRequest;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

@Component
public class RevenueAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.mers.projects.bbdgrad.com")
            .defaultHeader("Authorization", "Bearer MY_SECRET_TOKEN")
            .build();

    public Result<CreateRevenueResponse> registerTax(CreateRevenueRequest request) {
        try {
            return Result.success(
                    client
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                   .path("/api/taxpayer/business/register")
                                   .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(request))
                            .retrieve()
                            .bodyToMono(CreateRevenueResponse.class)
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Revenue Failed");
        }
    }

    public Result<CalculateRevenueResponse> calculateTax(CalculateRevenueRequest request) {
        try {
            return Result.success(
                    client
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                   .path("/api/taxcalculator/calculate")
                                   .queryParam("amount", request.getAmount())
                                   .queryParam("taxType", request.getTaxType())
                                   .build()
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(CalculateRevenueResponse.class)
                            .block()
            );
        } catch (Exception e) {
            // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
            e.printStackTrace();
            return Result.failure("Communication With Revenue Failed");
        }
    }
}

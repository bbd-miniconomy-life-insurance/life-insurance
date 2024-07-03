package bbd.miniconomy.lifeinsurance.services.api.handofzeus;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice.GetPriceResponse;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class HandOfZeusAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.zeus.projects.bbdgrad.com")
            .build();

        // Todo: return success or failure (try / catch)
    public Result<GetPriceResponse> getPriceOfLifeInsurance() {
        try {
                return Result.success(
                        client
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/life-insurance-price")
                                        .build()
                                )
                                .retrieve()
                                .bodyToMono(GetPriceResponse.class)
                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                                .block()
                );
        } catch (Exception e) {
                // TODO: Fix to use statusCode in WebClient and some retries in certain cases.
                e.printStackTrace();
                return Result.failure("Communication With Hand of Zeus Failed");
        }
    }
}

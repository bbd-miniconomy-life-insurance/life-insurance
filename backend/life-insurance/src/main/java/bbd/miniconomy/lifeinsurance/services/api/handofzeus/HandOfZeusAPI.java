package bbd.miniconomy.lifeinsurance.services.api.handofzeus;

import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.gettime.StartTimeResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice.GetPriceResponse;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class HandOfZeusAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.zeus.projects.bbdgrad.com")
            .build();


    public Result<GetPriceResponse> getPriceOfLifeInsurance() {
        try {
            return Result.success(
                    client
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/life-insurance")
                                    .build()
                            )
                            .retrieve()
                            .bodyToMono(GetPriceResponse.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                            .block()
            );
        } catch (RuntimeException e) {
            return Result.failure("Failed Communication with Zeus");
        }
    }

    public Result<LocalDateTime> getStartTime() {
        try {
            var responseObject = client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/start-time")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(StartTimeResponse.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                    .block();

            return Result.success(responseObject.getStartDate());
        } catch (RuntimeException e) {
            return Result.failure("Failed Communication with Zeus");
        }
    }
}

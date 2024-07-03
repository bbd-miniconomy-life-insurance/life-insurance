package bbd.miniconomy.lifeinsurance.services.api.handofzeus;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HandOfZeusAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.commercialbank.projects.bbdgrad.com")
            .build();

    public void getPriceOfLifeInsurance() {
        client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/life-insurance-price")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

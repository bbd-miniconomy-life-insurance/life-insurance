package bbd.miniconomy.lifeinsurance.services.api.revenue;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RevenueAPI {
    static final WebClient client = WebClient
            .builder()
            .baseUrl("https://api.mers.projects.bbdgrad.com")
            .defaultHeader("Authorization", "Bearer MY_SECRET_TOKEN")
            .build();
}

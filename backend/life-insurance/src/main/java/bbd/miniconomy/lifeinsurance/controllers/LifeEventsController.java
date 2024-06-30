package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.DeathDTO;
import bbd.miniconomy.lifeinsurance.models.dto.LifeEventsDTO;
import bbd.miniconomy.lifeinsurance.models.dto.Response;
import bbd.miniconomy.lifeinsurance.services.ClaimsService;
import bbd.miniconomy.lifeinsurance.services.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/life-events")
public class LifeEventsController {

    private final ClaimsService claimsService;
    private final PolicyService policyService;
    private final ThreadPoolTaskExecutor executor;

    public LifeEventsController(ClaimsService claimsService, PolicyService policyService, ThreadPoolTaskExecutor executor) {
        this.claimsService = claimsService;
        this.policyService = policyService;
        this.executor = executor;
    }

    @PostMapping
    public Response HandleLifeEvents(LifeEventsDTO lifeEvents) {

        executor.submit(() -> {
            for (DeathDTO death : lifeEvents.getDeaths()) {
                claimsService.payClaim(death);
            }
        });

        executor.submit(() -> {
            for (Long personaId : lifeEvents.getAdults()) {
                // TODO: remove null
                policyService.activatePolicy(personaId, null);
            }
        });

        // returns immediately
        return Response.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("Message received. Processing...")
                .build();
    }
}

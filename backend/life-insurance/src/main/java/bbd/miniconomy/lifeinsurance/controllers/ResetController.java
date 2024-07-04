package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.models.dto.reset.ResetDTO;
import bbd.miniconomy.lifeinsurance.services.ResetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control-simulation")
@Slf4j
public class ResetController {

    private final ThreadPoolTaskExecutor executor;
    private final ResetService resetService;

    public ResetController(ThreadPoolTaskExecutor executor, ResetService resetService) {
        this.executor = executor;
        this.resetService = resetService;
    }

    @PostMapping
    public ResponseEntity<GlobalLifeInsuranceResponse> ResetLifeInsuranceService(@RequestBody ResetDTO request) {
        log.info("Request received: {}", request);

        executor.submit(() -> resetService.resetSystem(request));

        return new ResponseEntity<>(
                GlobalLifeInsuranceResponse
                        .builder()
                        .status(HttpStatus.ACCEPTED.value())
                        .message("Life Insurance Successfully Reset")
                        .build(),
                HttpStatus.ACCEPTED
        );
    }
}
package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HealthController {

    @GetMapping
    public ResponseEntity<GlobalLifeInsuranceResponse> healthCheck() {
        return new ResponseEntity<>(
                GlobalLifeInsuranceResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("Service Healthy")
                        .build(),
                HttpStatus.OK
        );

    }

}

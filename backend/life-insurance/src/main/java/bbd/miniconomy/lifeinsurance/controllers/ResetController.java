package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.repositories.ResetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reset")
public class ResetController {

    private final ResetRepository resetRepository;

    public ResetController(ResetRepository resetRepository) {
        this.resetRepository = resetRepository;
    }

    @PostMapping
    public ResponseEntity<GlobalLifeInsuranceResponse> ResetLifeInsuranceService() {
        boolean success = resetRepository.resetDatabase();

        if (!success) {
            return new ResponseEntity<>(
                    GlobalLifeInsuranceResponse
                            .builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Life Insurance Successfully Reset")
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return new ResponseEntity<>(
                GlobalLifeInsuranceResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Life Insurance Successfully Reset")
                        .build(),
                HttpStatus.OK
        );
    }

}
package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.reset.ResetDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.TimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.repositories.ResetRepository;
import bbd.miniconomy.lifeinsurance.services.PolicyService;

@RestController
@RequestMapping("/control-simulation")
public class ResetController {

    private final ResetRepository resetRepository;
    private final PolicyService policyService;
    private final PriceRepository priceRepository;
    private final TimeService timeService;

    public ResetController(ResetRepository resetRepository, PolicyService policyService, PriceRepository priceRepository, TimeService timeService) {
        this.resetRepository = resetRepository;
        this.policyService = policyService;
        this.priceRepository = priceRepository;
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<GlobalLifeInsuranceResponse> ResetLifeInsuranceService(@RequestBody ResetDTO request) {
        // reset dbs
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

        // set time
        timeService.setStartTime(request.getStartTime());

        // add 0 to price table
        Price price = new Price();
        price.setPrice(0L);
        price.setInceptionDate(timeService.getGameTime());
        priceRepository.save(price);

        // get premium price from zeus
        policyService.setPolicyPrice();

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
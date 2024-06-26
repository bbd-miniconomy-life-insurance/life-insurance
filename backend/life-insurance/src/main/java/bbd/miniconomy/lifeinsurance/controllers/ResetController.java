package bbd.miniconomy.lifeinsurance.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reset")
public class ResetController {

    @PostMapping
    public void ResetLifeInsuranceService() {
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

}
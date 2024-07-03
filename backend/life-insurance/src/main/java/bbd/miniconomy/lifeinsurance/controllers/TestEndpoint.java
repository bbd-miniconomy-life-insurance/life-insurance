package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.services.TimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/date")
public class TestEndpoint {

    private final TimeService timeService;

    public TestEndpoint(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public LocalDateTime getDate(@RequestBody LocalDateTime start) {
        timeService.setStartTime(start);
        return timeService.getGameTime();
    }
}

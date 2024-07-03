package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class TimeService {
    private final ConstantsRepository constantsRepository;

    public TimeService(ConstantsRepository constantsRepository) {
        this.constantsRepository = constantsRepository;
    }

    public LocalDateTime getGameTime() {
        Constant startTimeString = constantsRepository.findByName("startDate");

        LocalDateTime startTime = LocalDateTime.parse(startTimeString.getId(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return calculateTimeFromStart(startTime);
    }

    public void setStartTime(LocalDateTime startTime) {
        // get the start date
        Constant startTimeString = constantsRepository.findByName("startDate");

        if (startTimeString != null) {
            return;
        }

        Constant constant = new Constant();
        constant.setName("startDate");
        constant.setId(String.valueOf(startTime));
        constantsRepository.save(constant);
    }

    private LocalDateTime calculateTimeFromStart(LocalDateTime startTime) {
        // Get the current day of the simulation (eg: day 1302)
        LocalDateTime current = LocalDateTime.now();
        long simulationDayNumber = (long) Math.floor(((double) Duration.between(startTime, current).getSeconds() / 120) + 1);

        // current year
        long year = (long) Math.floor(((double) simulationDayNumber / 360) + 1);
        long daysIntoYear = (long) Math.floor(simulationDayNumber % 360);

        // month and day
        var month = (long) Math.floor(((double) daysIntoYear / 30) + 1);
        var day = (daysIntoYear % 30);

        return LocalDateTime.of((int) year, (int) month, (int) day, 0, 0);
    }
}
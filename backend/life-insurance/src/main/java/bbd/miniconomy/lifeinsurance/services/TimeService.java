package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class TimeService {
    private final ConstantsRepository constantsRepository;
    private final RevenueService revenueService;
    private final StockExchangeService stockExchangeService;

    public TimeService(ConstantsRepository constantsRepository, RevenueService revenueService, StockExchangeService stockExchangeService) {
        this.constantsRepository = constantsRepository;
        this.revenueService = revenueService;
        this.stockExchangeService = stockExchangeService;
    }

    public LocalDateTime getGameTime() {
        Constant startTimeString = constantsRepository.findByName("startDate");

        if (startTimeString == null) {
            // we no have time.
            setStartTime(LocalDateTime.now());
            return getGameTime();
        }

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

    @Scheduled(fixedRate = 1000 * 60 * 2 * 30)
    public void runMonthlyTasks() {
        // tax
        revenueService.calculateTax(getMonthStart(), getMonthEnd());

        // dividends
        stockExchangeService.Dividence(getMonthStart(), getMonthEnd());

        // stocks buy
//        stockExchangeService.buyStocks();

    }

    public LocalDateTime getMonthStart() {
        LocalDateTime currentTime = getGameTime();
        return LocalDateTime.of(currentTime.getYear(), currentTime.getMonthValue(), 1, 0, 0);
    }

    public LocalDateTime getMonthEnd() {
        LocalDateTime currentTime = getGameTime();
        return LocalDateTime.of(currentTime.getYear(), currentTime.getMonthValue(), 30, 0, 0);
    }
}
package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.reset.ResetDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Price;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.repositories.ResetRepository;
import org.springframework.stereotype.Service;

@Service
public class ResetService {

    private final ResetRepository resetRepository;
    private final PolicyService policyService;
    private final PriceRepository priceRepository;
    private final TimeService timeService;
    private final RevenueService revenueService;

    public ResetService(ResetRepository resetRepository, PolicyService policyService, PriceRepository priceRepository, TimeService timeService, RevenueService revenueService) {
        this.resetRepository = resetRepository;
        this.policyService = policyService;
        this.priceRepository = priceRepository;
        this.timeService = timeService;
        this.revenueService = revenueService;
    }

    public void resetSystem(ResetDTO request) {
        // reset DB
        boolean success = resetRepository.resetDatabase();

        if (!success) {
            return;
        }

        // set time
        timeService.setStartTime(request.getStartTime());

        setDefaultPrice();

        // get premium price from zeus
        policyService.setPolicyPrice();

        // get tax id from SARS
        revenueService.registerTax();
    }

    private void setDefaultPrice() {
        Price price = new Price();
        price.setPrice(0L);
        price.setInceptionDate(timeService.getGameTime());
        priceRepository.save(price);
    }

}

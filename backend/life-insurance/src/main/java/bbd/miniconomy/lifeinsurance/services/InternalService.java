import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class InternalService {
    private final PolicyRepository policyRepository;
    private final PriceRepository priceRepository;
    private final double probabilityOfDeathInMonth = 0.1;

    @Autowired
    public InternalService(PolicyRepository policyRepository, PriceRepository priceRepository) {
        this.policyRepository = policyRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional
    public void Taxes() {
        // Not sure how this works yet
    }

    @Transactional
    public void BuyStocksWithAvailableMoney() {
        double moneyForStocks = EstimatedMonthlyIncome() * 0.3;

        // Buy stocks
    }

    public void SellStocks(){
        
    }

    private double EstimatedMonthlyIncome(){
        long activatePoliciesCount = policyRepository.countActivePolicies();
        double currentPremiumPrice = priceRepository.findFirstByOrderByInceptionDateDesc();
        
        double moneyIn = activatePoliciesCount * currentPremiumPrice;
        double moneyOut = activatePoliciesCount * currentPremiumPrice * 30 * probabilityOfDeathInMonth;

        return Math.max(0, moneyIn - moneyOut);
    }
}

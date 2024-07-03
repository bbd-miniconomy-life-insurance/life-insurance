package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.StatusName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.StockListingResponse;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InternalService {
    private final PolicyRepository policyRepository;
    private final PriceRepository priceRepository;
    private final Long probabilityOfDeathInMonth = (long)0.1;
    private final StockExchangeService stockExchangeService;

    @Autowired
    public InternalService(PolicyRepository policyRepository, PriceRepository priceRepository, StockExchangeService stockExchangeService) {
        this.policyRepository = policyRepository;
        this.priceRepository = priceRepository;
        this.stockExchangeService = stockExchangeService;
    }

    @Transactional
    public void Taxes() {
        // Not sure how this works yet
    }

    @Transactional
    public void BuyStocksWithAvailableMoney() {
        Long moneyForStocks = (long)(EstimatedMonthlyIncome() * 0.3);

        var stockListings = stockExchangeService.stockListing();

        if (stockListings.isFailure()) {
            return;
        }

        while (moneyForStocks > 0) {
            Optional<StockListingResponse> stockListing = stockListings
                .getValue()
                .stream()
                .filter(sl -> sl.getQuantity() > 0)
                .sorted((sl1, sl2) -> {
                    double totalAvailableStock1 = sl1.getCurrentMarketValue() * sl1.getQuantity();
                    double totalAvailableStock2 = sl2.getCurrentMarketValue() * sl2.getQuantity();
                    return Double.compare(totalAvailableStock2, totalAvailableStock1);
                })
                .findFirst();

            if (stockListing.isPresent()) {
                StockListingResponse stockListingResponse = stockListing.get();

                Result<BuyStockResponse> buyStocksResponse = stockExchangeService.buyStocks(stockListingResponse.getBusinessId(), moneyForStocks);

                if (buyStocksResponse.isFailure()) {
                    return;
                }

                moneyForStocks -= buyStocksResponse.getValue().getAmountToPay();

                //transaction
                //stockRepository.InsertStock(stockListingResponse.getBusinessId(), buyStocksResponse.getValue().getAmountToPay()); // ToDo
            }
            else 
            {
                return;
            }
        }
    }

    public void SellStocks(Long requiredAmount){
        var stillRequiredAmount = requiredAmount;
        var stockListings = stockExchangeService.stockListing();

        if (stockListings.isFailure()) {
            return;
        }
        while (stillRequiredAmount > 0) {
            // Get owned stock return if none
            // Calculate total value
            // Max(value, stillRequiredAmount)
            // Sell on stocks, sell on bank
            // update our stock table
            // return or try sell more


            //stockRepository.GetOwnedStock
            //if no stock return
            stockListings.getValue().stream().filter(stockListing -> stockListing.getBusinessId() == ownedStockBusinessId);
            // amountOwned *
            stockExchangeService.sellStocks(null, null);
            //Update stock table
            //stillRequiredAmount - 
        }
    }

    private Long EstimatedMonthlyIncome(){
        Long activatePoliciesCount = policyRepository.countByStatus_StatusName(StatusName.Active);
        Long currentPremiumPrice = priceRepository.findFirstByOrderByInceptionDateDesc().getPrice();
        
        Long moneyIn = activatePoliciesCount * currentPremiumPrice;
        Long moneyOut = activatePoliciesCount * currentPremiumPrice * 30 * probabilityOfDeathInMonth;

        return Math.max(0, moneyIn - moneyOut);
    }
}

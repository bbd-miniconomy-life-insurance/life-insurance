package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.entities.Stock;
import bbd.miniconomy.lifeinsurance.repositories.PolicyRepository;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.repositories.StockRepository;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.StockListingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternalService {
    private final StockRepository stockRepository;
    private final PolicyRepository policyRepository;
    private final PriceRepository priceRepository;
    private final Long probabilityOfDeathInMonth = (long)0.1;
    private final StockExchangeService stockExchangeService;
    private final CommercialBankService commercialBankService;

    @Autowired
    public InternalService(PolicyRepository policyRepository, PriceRepository priceRepository, StockExchangeService stockExchangeService, CommercialBankService commercialBankService, StockRepository stockRepository) {
        this.policyRepository = policyRepository;
        this.priceRepository = priceRepository;
        this.stockExchangeService = stockExchangeService;
        this.commercialBankService = commercialBankService;
        this.stockRepository = stockRepository;
    }

    public void Taxes() {
        // Not sure how this works yet
    }

    public void BuyStocksWithAvailableMoney() {
        Long moneyForStocks = (long)(EstimatedMonthlyIncome() * 0.3);

        var stockListings = stockExchangeService.stockListing();

        if (stockListings.isFailure()) {
            return;
        }

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

            stockExchangeService.buyStocks(stockListingResponse.getBusinessId(), moneyForStocks);
        }
        else 
        {
            return;
        }
    }

    public void SellStocks(Long requiredAmount){
        var stillRequiredAmount = requiredAmount;
        var stockListings = stockExchangeService.stockListing();

        if (stockListings.isFailure()) {
            return;
        }

        while (stillRequiredAmount > 0) {
            var firstStock = stockRepository.findFirstByOrderByQuantityDesc();

            if (firstStock.getQuantity() <= 0) {
                return;
            }

            var stockListing = stockListings
                .getValue()
                .stream()
                .filter(sl -> sl.getBusinessId() == firstStock.getBusinessId()).findFirst();

            if (stockListing.isPresent()) {
                var stockTotalValue = stockListing.get().getCurrentMarketValue() * firstStock.getQuantity();
                stockTotalValue = Math.min(stillRequiredAmount, stockTotalValue);
                Integer quantityToSell = (int)Math.ceil((stockTotalValue / stockListing.get().getCurrentMarketValue()));

                var sellStocksResult = stockExchangeService.sellStocks(firstStock.getBusinessId(), quantityToSell);

                if (sellStocksResult.isFailure()){
                    return;
                }

                stillRequiredAmount -= quantityToSell * stockListing.get().getCurrentMarketValue();

                firstStock.setQuantity(firstStock.getQuantity() - quantityToSell);
                stockRepository.save(firstStock);
            }
            else{
                return;
            }
        }
    }

    private Long EstimatedMonthlyIncome(){
        Long activatePoliciesCount = policyRepository.countByStatus_StatusName("Active");
        Long currentPremiumPrice = priceRepository.findTopByOrderByIdDesc().getPrice();
        
        Long moneyIn = activatePoliciesCount * currentPremiumPrice;
        Long moneyOut = activatePoliciesCount * currentPremiumPrice * 30 * probabilityOfDeathInMonth;

        return Math.max(0, moneyIn - moneyOut);
    }
}

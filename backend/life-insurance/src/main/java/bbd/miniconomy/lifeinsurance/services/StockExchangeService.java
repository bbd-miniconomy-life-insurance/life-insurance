package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.ConstantName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import bbd.miniconomy.lifeinsurance.models.entities.Stock;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.CreateBusinessRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.CreateBusinessResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.SellStockRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.SellStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.StockListingResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockExchangeService {
    private final APILayer communicationLayer; 
    private final ConstantsRepository constantsRepository;

    public StockExchangeService(APILayer apiLayer, ConstantsRepository constantsRepository) {
        this.communicationLayer = apiLayer;
        this.constantsRepository = constantsRepository;
    }

    public void registerBusiness(){
        var createBusinessRequest = CreateBusinessRequest
        .builder()
        .name("LifeInsurance")
        .bankAccount("life-insurance")
        .build();

        var result = communicationLayer
        .getStockExchangeAPI()
        .registerBusiness(createBusinessRequest);

        if (result.isFailure())
        {
            return;
        }

        var constant = Constant
            .builder()
            .id(result
                .getValue()
                .getId())
            .name(ConstantName.tradingID.toString())
            .build();
        constantsRepository.save(constant);
    }

    public Result<SellStockResponse> sellStocks(String companyId, Integer quantity){
        String tradingID = constantsRepository.findByName(ConstantName.tradingID.toString()).getId();

        if (tradingID == null){
            // Todo
            // registerBusiness();
            return Result.failure("No TradingID");
        }

        var sellStockRequest = SellStockRequest
            .builder()
            .sellerId(tradingID)
            .companyId(companyId) // The ID of the business whose stock is being sold.
            .quantity(quantity)
            .build();

        return communicationLayer
        .getStockExchangeAPI()
        .sellStocks(sellStockRequest);
    }

    public Result<BuyStockResponse> buyStocks(String businessId, Long maxPrice){
        String tradingID = constantsRepository.findByName(ConstantName.tradingID.toString()).getId();

        if (tradingID == null){
            // Todo
            // registerBusiness();
            return Result.failure("No TradingID");
        }
        var buyStockRequest = BuyStockRequest
            .builder()
            .buyerId(tradingID)
            .businessId(businessId) // The ID of the business whose stock is being bought.
            .maxPrice(maxPrice)
            .build();

        return communicationLayer
        .getStockExchangeAPI()
        .buyStocks(buyStockRequest);
    }

    public Result<List<StockListingResponse>> stockListing(){
        return communicationLayer
        .getStockExchangeAPI()
        .stockListing();
    }

    public Result<DividendsResponse> Dividence(Long amount){
        var dividendsRequest = DividendsRequest
        .builder()
        .businessId("")// get from db *The ID of the business requesting dividends distribution
        .amount(amount)
        .build();

        return communicationLayer
            .getStockExchangeAPI()
            .Dividends(dividendsRequest);
    }

}

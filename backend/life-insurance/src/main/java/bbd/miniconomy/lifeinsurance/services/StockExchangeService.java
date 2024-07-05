package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.enums.ConstantName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import bbd.miniconomy.lifeinsurance.models.entities.Transaction;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import bbd.miniconomy.lifeinsurance.repositories.TransactionHistoryRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.CreateBusinessRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.SellStockRequest;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.SellStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.StockListingResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockRequest;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockExchangeService {
    private final APILayer communicationLayer; 
    private final ConstantsRepository constantsRepository;
    private final CommercialBankService commercialBankService;
    private final TransactionHistoryRepository historyRepository;

    public StockExchangeService(APILayer apiLayer, ConstantsRepository constantsRepository, CommercialBankService commercialBankService, TransactionHistoryRepository historyRepository) {
        this.communicationLayer = apiLayer;
        this.constantsRepository = constantsRepository;
        this.commercialBankService = commercialBankService;
        this.historyRepository = historyRepository;
    }

    public void registerBusiness(){
        var createBusinessRequest = CreateBusinessRequest
        .builder()
        .name("LifeInsurance")
        .bankAccount("life-insurance")
        .build();

        communicationLayer
        .getStockExchangeAPI()
        .registerBusiness(createBusinessRequest);
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

    public void buyStocks(String businessId, Long maxPrice){
        Constant constant = constantsRepository.findByName(ConstantName.tradingID.toString());

        if (constant == null){
            // Todo
            // registerBusiness();
            return;
        }

        String tradingID = constant.getId();

        var buyStockRequest = BuyStockRequest
            .builder()
            .buyerId(tradingID)
            .businessId(businessId) // The ID of the business whose stock is being bought.
            .maxPrice(maxPrice)
            .build();

        communicationLayer
            .getStockExchangeAPI()
            .buyStocks(buyStockRequest);
    }

    public Result<List<StockListingResponse>> stockListing(){
        return communicationLayer
        .getStockExchangeAPI()
        .stockListing();
    }

    public Result<DividendsResponse> Dividence(LocalDateTime monthStart, LocalDateTime monthEnd){
        Constant constant = constantsRepository.findByName(ConstantName.tradingID.toString());

        if (constant == null){
            // Todo
            // registerBusiness();
            return Result.failure("No TradingID");
        }

        String tradingID = constant.getId();

        Long amount = historyRepository.findSumOfTransactionsFromLastMonth(monthStart, monthEnd);

        var dividendsRequest = DividendsRequest
        .builder()
        .businessId(tradingID)
        .amount(amount)
        .build();

        var dividendsResponse = communicationLayer
            .getStockExchangeAPI()
            .Dividends(dividendsRequest);

        if (dividendsResponse.isFailure()) {
            return dividendsResponse;
        }

        var validTransactions = List.of(CreateTransactionRequestTransaction
            .builder()
            .debitAccountName("stock-exchange")
            .creditAccountName("life-insurance")
            .amount(amount)
            .debitRef(dividendsResponse.getValue().getReferenceId())
            .creditRef("Bought Stocks")
            .build());

        commercialBankService.createTransactions(validTransactions);

        return dividendsResponse;
    }

}

package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.enums.ConstantName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.dto.GlobalLifeInsuranceResponse;
import bbd.miniconomy.lifeinsurance.models.dto.paymentnotification.PaymentNotificationDTO;
import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import bbd.miniconomy.lifeinsurance.models.entities.Stock;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import bbd.miniconomy.lifeinsurance.repositories.ResetRepository;
import bbd.miniconomy.lifeinsurance.repositories.StockRepository;
import bbd.miniconomy.lifeinsurance.services.CommercialBankService;
import bbd.miniconomy.lifeinsurance.services.PolicyService;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.BuyStockResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.CreateBusinessResponse;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.models.DividendsResponse;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockExchangeController {
    private final CommercialBankService commercialBankService;
    private final StockRepository stockRepository;
    private final ConstantsRepository constantsRepository;

    public StockExchangeController(CommercialBankService commercialBankService, StockRepository stockRepository, ConstantsRepository constantsRepository) {
        this.commercialBankService = commercialBankService;
        this.stockRepository = stockRepository;
        this.constantsRepository = constantsRepository;
    }

    @PostMapping("/register-callback")
    public void registerCallback(@RequestBody CreateBusinessResponse createBusinessResponse){
        var constant = Constant
            .builder()
            .id(createBusinessResponse
                .getId())
            .name(ConstantName.tradingID.toString())
            .build();
        constantsRepository.save(constant);
    }

    @PostMapping("/buy-callback")
    public void buystockcallback(@RequestBody BuyStockResponse buyStockResponse) {
            var validTransactions = List.of(CreateTransactionRequestTransaction
                    .builder()
                    .debitAccountName("stock-exchange")
                    .creditAccountName("life-insurance")
                    .amount(buyStockResponse.getAmountToPay())
                    .debitRef(buyStockResponse.getReferenceId())
                    .creditRef("Bought Stocks")
                    .build());
                    
            Result<CreateTransactionResponse> transactionsResult = commercialBankService.createTransactions(validTransactions);
                    
            if (transactionsResult.isFailure()) {
                return;
            }

            Stock stock = stockRepository.findByBusinessId(buyStockResponse.getReferenceId());

            if (stock == null){
                stock = Stock.builder()
                    .businessId(buyStockResponse.getReferenceId())
                    .quantity(buyStockResponse.getQuantity())
                    .build();
            }
            else{
                stock.setQuantity(stock.getQuantity() + buyStockResponse.getQuantity());
            }

            stockRepository.save(stock);
    }
}

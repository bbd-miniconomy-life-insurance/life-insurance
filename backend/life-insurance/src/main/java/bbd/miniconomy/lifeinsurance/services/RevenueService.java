package bbd.miniconomy.lifeinsurance.services;

import org.springframework.stereotype.Service;

import bbd.miniconomy.lifeinsurance.enums.ConstantName;
import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.models.entities.Constant;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import bbd.miniconomy.lifeinsurance.repositories.TransactionHistoryRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionRequestTransaction;
import bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions.CreateTransactionResponse;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CalculateRevenueRequest;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CalculateRevenueResponse;
import bbd.miniconomy.lifeinsurance.services.api.revenue.models.CreateRevenueRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RevenueService {
    private final APILayer communicationLayer; 
    private final ConstantsRepository constantsRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final CommercialBankService commercialBankService;

    public RevenueService(APILayer apiLayer, ConstantsRepository constantsRepository, TransactionHistoryRepository transactionHistoryRepository, CommercialBankService commercialBankService) {
        this.communicationLayer = apiLayer;
        this.constantsRepository = constantsRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.commercialBankService = commercialBankService;
    }

    public void registerTax(){
        var createRevenueRequest = CreateRevenueRequest
            .builder()
            .businessName("life_insurance")
            .build();

        var result = communicationLayer
            .getRevenueAPI()
            .registerTax(createRevenueRequest);

        if (result.isFailure())
        {
            return;
        }

        var constant = Constant
            .builder()
            .id(result
                .getValue()
                .getTaxId())
            .name(ConstantName.taxId.toString())
            .build();
        constantsRepository.save(constant);
    }

    public Result<CalculateRevenueResponse> calculateTax(LocalDateTime startMonth, LocalDateTime endMonth){

        var calculateRevenueRequest = CalculateRevenueRequest
            .builder()
            // .amount(transactionHistoryRepository.findSumOfTransactionsFromLastMonth(null, null))
            .amount(0.0)
            .taxType("INCOME")
            .build();

        return communicationLayer
            .getRevenueAPI()
            .calculateTax(calculateRevenueRequest);

        // TODO pay tax.
    }

    public void payTax(long amount) {
        var validTransactions = List.of(CreateTransactionRequestTransaction
                    .builder()
                    .debitAccountName("central_revenue") // TODO: replace with real name
                    .creditAccountName("life_insurance")
                    .amount(amount)
                    .debitRef("Life Insurance paid tax")
                    .creditRef("Paid tax")
                    .build());
                    
            Result<CreateTransactionResponse> transactionsResult = commercialBankService.createTransactions(validTransactions);
                    
            if (transactionsResult.isFailure()) {
                return;
            }
        }
}

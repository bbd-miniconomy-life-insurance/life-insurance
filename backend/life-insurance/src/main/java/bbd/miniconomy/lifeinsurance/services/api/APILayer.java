package bbd.miniconomy.lifeinsurance.services.api;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.retailbank.RetailBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.revenue.RevenueAPI;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.StockExchangeAPI;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class APILayer {
    private final CommercialBankAPI commercialBankAPI;
    private final RetailBankAPI retailBankAPI;
    private final RevenueAPI revenueAPI;
    private final StockExchangeAPI stockExchangeAPI;

    APILayer(
            CommercialBankAPI commercialBankAPI,
            RetailBankAPI retailBankAPI,
            RevenueAPI revenueAPI,
            StockExchangeAPI stockExchangeAPI
    ) {
        this.commercialBankAPI = commercialBankAPI;
        this.retailBankAPI = retailBankAPI;
        this.revenueAPI = revenueAPI;
        this.stockExchangeAPI = stockExchangeAPI;
    }

}

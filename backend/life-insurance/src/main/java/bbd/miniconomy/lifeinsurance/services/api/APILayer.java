package bbd.miniconomy.lifeinsurance.services.api;

import org.springframework.stereotype.Component;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.HandOfZeusAPI;
import bbd.miniconomy.lifeinsurance.services.api.revenue.RevenueAPI;
import bbd.miniconomy.lifeinsurance.services.api.stockexchange.StockExchangeAPI;
import lombok.Getter;

@Component
@Getter
public class APILayer {
    private final CommercialBankAPI commercialBankAPI;
    private final RevenueAPI revenueAPI;
    private final HandOfZeusAPI handOfZeusAPI;
    private final StockExchangeAPI stockExchangeAPI;

    APILayer(
            CommercialBankAPI commercialBankAPI,
            RevenueAPI revenueAPI,
            HandOfZeusAPI handOfZeusAPI,
            StockExchangeAPI stockExchangeAPI
    ) {
        this.commercialBankAPI = commercialBankAPI;
        this.revenueAPI = revenueAPI;
        this.handOfZeusAPI = handOfZeusAPI;
        this.stockExchangeAPI = stockExchangeAPI;
    }

}

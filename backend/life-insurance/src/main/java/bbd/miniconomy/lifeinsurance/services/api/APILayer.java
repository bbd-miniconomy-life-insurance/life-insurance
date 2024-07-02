package bbd.miniconomy.lifeinsurance.services.api;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.retailbank.RetailBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.revenue.RevenueAPI;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class APILayer {
    private final CommercialBankAPI commercialBankAPI;
    private final RetailBankAPI retailBankAPI;
    private final RevenueAPI revenueAPI;

    APILayer(
            CommercialBankAPI commercialBankAPI,
            RetailBankAPI retailBankAPI,
            RevenueAPI revenueAPI
    ) {
        this.commercialBankAPI = commercialBankAPI;
        this.retailBankAPI = retailBankAPI;
        this.revenueAPI = revenueAPI;
    }

}

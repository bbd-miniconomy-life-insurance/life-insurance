package bbd.miniconomy.lifeinsurance.services.api;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.retailbank.RetailBankAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APILayer {
    private final CommercialBankAPI commercialBankAPI;
    private final RetailBankAPI retailBankAPI;

    APILayer(
            @Autowired CommercialBankAPI commercialBankAPI,
            @Autowired RetailBankAPI retailBankAPI
    ) {
        this.commercialBankAPI = commercialBankAPI;
        this.retailBankAPI = retailBankAPI;
    }

    public CommercialBankAPI getCommercialBankAPI() {
        return commercialBankAPI;
    }

    public RetailBankAPI getRetailBankAPI() {
        return retailBankAPI;
    }
}

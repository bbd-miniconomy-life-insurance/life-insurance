package bbd.miniconomy.lifeinsurance.services.api;

import org.springframework.stereotype.Component;

import bbd.miniconomy.lifeinsurance.services.api.commercialbank.CommercialBankAPI;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.HandOfZeusAPI;
import bbd.miniconomy.lifeinsurance.services.api.revenue.RevenueAPI;
import lombok.Getter;

@Component
@Getter
public class APILayer {
    private final CommercialBankAPI commercialBankAPI;
    private final RevenueAPI revenueAPI;
    private final HandOfZeusAPI handOfZeusAPI;

    APILayer(
            CommercialBankAPI commercialBankAPI,
            RevenueAPI revenueAPI,
            HandOfZeusAPI handOfZeusAPI
    ) {
        this.commercialBankAPI = commercialBankAPI;
        this.revenueAPI = revenueAPI;
        this.handOfZeusAPI = handOfZeusAPI;
    }

}

package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import org.springframework.beans.factory.annotation.Autowired;

public class CommercialBankService {
    private final APILayer apiLayer;

    public CommercialBankService(@Autowired APILayer apiLayer) {
        this.apiLayer = apiLayer;
    }

    public void createTransaction() {
        apiLayer.getCommercialBankAPI().createTransaction(null);
    }
}

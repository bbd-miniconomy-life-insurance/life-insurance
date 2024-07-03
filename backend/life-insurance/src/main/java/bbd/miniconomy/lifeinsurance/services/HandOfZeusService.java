package bbd.miniconomy.lifeinsurance.services;

import org.springframework.stereotype.Service;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.repositories.PriceRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice.GetPriceResponse;


@Service
public class HandOfZeusService {
    private final APILayer communicationLayer;

    public HandOfZeusService(APILayer apiLayer, PriceRepository priceRepository) {
        this.communicationLayer = apiLayer;
    }

    // Todo: method to map HOZ request and call API
    public Result<GetPriceResponse> getPriceOfLifeInsurance() {
        return communicationLayer.getHandOfZeusAPI().getPriceOfLifeInsurance();
    }
}
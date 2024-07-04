package bbd.miniconomy.lifeinsurance.services;

import org.springframework.stereotype.Service;

import bbd.miniconomy.lifeinsurance.models.Result;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;
import bbd.miniconomy.lifeinsurance.services.api.handofzeus.models.getprice.GetPriceResponse;

import java.time.LocalDateTime;


@Service
public class HandOfZeusService {
    private final APILayer communicationLayer;

    public HandOfZeusService(APILayer apiLayer) {
        this.communicationLayer = apiLayer;
    }

    public Result<GetPriceResponse> getPriceOfLifeInsurance() {
        return communicationLayer.getHandOfZeusAPI().getPriceOfLifeInsurance();
    }

    public Result<LocalDateTime> getStartTime() {
        return communicationLayer.getHandOfZeusAPI().getStartTime();
    }
}

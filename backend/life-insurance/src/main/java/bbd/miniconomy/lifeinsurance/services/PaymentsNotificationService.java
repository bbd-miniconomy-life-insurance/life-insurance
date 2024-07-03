package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.paymentnotification.PaymentNotificationDTO;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;

import org.springframework.stereotype.Service;

@Service
public class PaymentsNotificationService {
    private final InternalService internalService;

    public PaymentsNotificationService(InternalService internalService) {
        this.internalService = internalService;
    }

    public void handleIncomingPayments(PaymentNotificationDTO notification) {

    }
    
    public void handleOutgoingPayments(PaymentNotificationDTO notification) {
        
    }

    public void handleFailedTransactions(PaymentNotificationDTO notification) {

    }
}

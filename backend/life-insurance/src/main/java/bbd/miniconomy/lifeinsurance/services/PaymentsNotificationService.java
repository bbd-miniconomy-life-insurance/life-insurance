package bbd.miniconomy.lifeinsurance.services;

import bbd.miniconomy.lifeinsurance.models.dto.paymentnotification.PaymentNotificationDTO;
import bbd.miniconomy.lifeinsurance.models.entities.TransactionHistory;
import bbd.miniconomy.lifeinsurance.repositories.ConstantsRepository;
import bbd.miniconomy.lifeinsurance.repositories.TransactionHistoryRepository;
import bbd.miniconomy.lifeinsurance.services.api.APILayer;

import org.springframework.stereotype.Service;

@Service
public class PaymentsNotificationService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TimeService timeService;

    public PaymentsNotificationService(TimeService timeService, TransactionHistoryRepository transactionHistoryRepository) {
        this.timeService = timeService;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public void handleIncomingPayments(PaymentNotificationDTO notification) {
        var transactionHistory = TransactionHistory
            .builder()
            .amount(notification.getTransaction().getAmount())
            .reference(notification.getTransaction().getReference())
            .date(timeService.getGameTime())
            .build();
        transactionHistoryRepository.save(transactionHistory);
    }
    
    public void handleOutgoingPayments(PaymentNotificationDTO notification) {
        var transactionHistory = TransactionHistory
            .builder()
            .amount(notification.getTransaction().getAmount() * -1)
            .reference(notification.getTransaction().getReference())
            .date(timeService.getGameTime())
            .build();
        transactionHistoryRepository.save(transactionHistory);
    }

    public void handleFailedTransactions(PaymentNotificationDTO notification) {

    }
}

package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.paymentnotification.PaymentNotificationDTO;
import bbd.miniconomy.lifeinsurance.services.PaymentsNotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-notifications")
public class PaymentNotificationController {

    private final PaymentsNotificationService paymentsNotificationService;

    public PaymentNotificationController(PaymentsNotificationService paymentsNotificationService) {
        this.paymentsNotificationService = paymentsNotificationService;
    }

    @PostMapping
    public void receivePaymentNotifications(@RequestBody PaymentNotificationDTO notification) {
        // get matching transaction from the db - can be the debit and credit tho...
        // figure out by if we go paid or if we are paying.
        switch (notification.getType()) {
            case INCOMING_PAYMENT -> {
                paymentsNotificationService.handleIncomingPayments(notification);
                return;
            }

            case OUTGOING_PAYMENT -> {
                paymentsNotificationService.handleOutgoingPayments(notification);
                return;
            }

            case TRANSACTION_FAILED -> {
                paymentsNotificationService.handleFailedTransactions(notification);
                return;
            }

            case null, default -> {
                return;

            }
        }
    }
}

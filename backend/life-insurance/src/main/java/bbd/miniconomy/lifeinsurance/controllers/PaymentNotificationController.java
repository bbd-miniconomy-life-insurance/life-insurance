package bbd.miniconomy.lifeinsurance.controllers;

import bbd.miniconomy.lifeinsurance.models.dto.paymentnotification.PaymentNotificationDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-notifications")
public class PaymentNotificationController {

    @PostMapping
    public void receivePaymentNotifications(PaymentNotificationDTO notification) {
        // get notification
        // update the stuff based on what exactly happened.
        // incoming payment - that means that someone paid their debit order.
        // will have transaction id for this.
    }
}

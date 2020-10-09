package org.inmediart.mail.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    String USER_REGISTRATION = "userRegistrationChannel";
    String USER_CANCELLATION = "userCancellationChannel";
    String USER_ORDER = "userOrderChannel";
    String RECHARGE_USER_CREDIT = "rechargeUserCreditChannel";
    String ORDER_PAYMENT_CONFIRMATION = "orderPaymentConfirmationChannel";
    String ORDER_UPDATE = "orderUpdateChannel";
    String ORDER_CANCELLATION = "orderCancellationChannel";
    String PRODUCT_UPDATE = "productUpdateChannel";

    @Input(USER_REGISTRATION)
    SubscribableChannel userRegistrationChannel();

    @Input(USER_CANCELLATION)
    SubscribableChannel userCancellationChannel();

    @Input(USER_ORDER)
    SubscribableChannel userOrderChannel();

    @Input(ORDER_UPDATE)
    MessageChannel orderUpdateChannel();

    @Input(RECHARGE_USER_CREDIT)
    SubscribableChannel rechargeUserCreditChannel();

    @Input(ORDER_PAYMENT_CONFIRMATION)
    SubscribableChannel orderPaymentConfirmationChannel();

    @Input(ORDER_CANCELLATION)
    SubscribableChannel orderCancellationChannel();

    @Input(PRODUCT_UPDATE)
    SubscribableChannel productUpdateChannel();
}

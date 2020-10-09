package org.inmediart.gui.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GUIMQBinding {
    String USER_REGISTRATION = "userRegistrationChannel";
    String USER_CANCELLATION = "userCancellationChannel";
    String USER_ORDER = "userOrderChannel";
    String ORDER_CANCELLATION = "orderCancellationChannel";
    String ORDER_UPDATE = "orderUpdateChannel";
    String ORDER_PAYMENT_CONFIRMATION = "orderPaymentConfirmationChannel";
    String RECHARGE_USER_CREDIT = "rechargeUserCreditChannel";
    String PRODUCT_UPDATE = "productUpdateChannel";

    @Input(USER_REGISTRATION)
    SubscribableChannel userRegistrationChannel();

    @Input(USER_CANCELLATION)
    SubscribableChannel userCancellationChannel();

    @Input(USER_ORDER)
    SubscribableChannel userOrderChannel();

    @Input(ORDER_CANCELLATION)
    SubscribableChannel orderCancellationChannel();

    @Input(ORDER_UPDATE)
    SubscribableChannel orderUpdateChannel();

    @Input(ORDER_PAYMENT_CONFIRMATION)
    SubscribableChannel userOrderPaymentConfirmationChannel();

    @Input(RECHARGE_USER_CREDIT)
    SubscribableChannel rechargeUserCreditChannel();

    @Input(PRODUCT_UPDATE)
    SubscribableChannel productUpdateChannel();
}

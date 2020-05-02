package org.inmediart.commons.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.SubscribableChannel;

public interface GUIMQBinding {
    String USER_REGISTRATION = "userRegistrationInputChannel";
    String USER_CANCELLATION = "userCancellationInputChannel";
    String USER_ORDER = "userOrderInputChannel";
    String ORDER_CANCELLATION = "orderCancellationInputChannel";
    String ORDER_UPDATE = "orderUpdateInputChannel";
    String ORDER_PAYMENT_CONFIRMATION = "orderPaymentConfirmationInputChannel";
    String RECHARGE_USER_CREDIT = "rechargeUserCreditInputChannel";

    @Input(USER_REGISTRATION)
    SubscribableChannel userRegistrationInputChannel();

    @Input(USER_CANCELLATION)
    SubscribableChannel userCancellationInputChannel();

    @Input(USER_ORDER)
    SubscribableChannel userOrderInputChannel();

    @Input(ORDER_CANCELLATION)
    SubscribableChannel orderCancellationInputChannel();

    @Input(ORDER_UPDATE)
    SubscribableChannel orderUpdateInputChannel();

    @Input(ORDER_PAYMENT_CONFIRMATION)
    SubscribableChannel userOrderPaymentConfirmationInputChannel();

    @Input(RECHARGE_USER_CREDIT)
    SubscribableChannel rechargeUserCreditInputChannel();
}

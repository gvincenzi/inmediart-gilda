package org.inmediart.api.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface RestAPIMQBinding {
    String USER_REGISTRATION = "userRegistrationChannel";
    String USER_CANCELLATION = "userCancellationChannel";
    String USER_ORDER = "userOrderChannel";
    String ORDER_CANCELLATION = "orderCancellationChannel";
    String ORDER_UPDATE = "orderUpdateChannel";
    String ORDER_PAYMENT_CONFIRMATION = "orderPaymentConfirmationChannel";
    String EXTERNAL_PRODUCT = "externalProductChannel";
    String EXTERNAL_PRODUCT_INPUT = "externalProductInputChannel";
    String ADVERTISING = "advertisingChannel";
    String RECHARGE_USER_CREDIT = "rechargeUserCreditChannel";

    @Output(USER_REGISTRATION)
    MessageChannel userRegistrationChannel();

    @Output(USER_CANCELLATION)
    MessageChannel userCancellationChannel();

    @Output(USER_ORDER)
    MessageChannel userOrderChannel();

    @Output(ORDER_CANCELLATION)
    MessageChannel orderCancellationChannel();

    @Output(ORDER_UPDATE)
    MessageChannel orderUpdateChannel();

    @Output(ORDER_PAYMENT_CONFIRMATION)
    MessageChannel userOrderPaymentConfirmationChannel();

    @Output(ADVERTISING)
    MessageChannel advertisingChannel();

    @Output(EXTERNAL_PRODUCT)
    MessageChannel externalProductChannel();

    @Output(RECHARGE_USER_CREDIT)
    MessageChannel rechargeUserCreditChannel();

    @Input(EXTERNAL_PRODUCT_INPUT)
    SubscribableChannel externalProductInputChannel();
}

package org.inmediart.commons.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MQBinding {
    String USER_REGISTRATION = "userRegistrationChannel";
    String USER_CANCELLATION = "userCancellationChannel";
    String USER_ORDER = "userOrderChannel";
    String ORDER_CANCELLATION = "orderCancellationChannel";
    String ORDER_UPDATE = "orderUpdateChannel";
    String ORDER_PAYMENT = "orderPaymentChannel";
    String ORDER_PAYMENT_CONFIRMATION = "orderPaymentConfirmationChannel";
    String EXTERNAL_PRODUCT = "externalProductChannel";
    String EXTERNAL_PRODUCT_INPUT = "externalProductInputChannel";
    String ADVERTISING = "advertisingChannel";

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

    @Input(ORDER_PAYMENT)
    SubscribableChannel orderPaymentChannel();

    @Output(ORDER_PAYMENT_CONFIRMATION)
    MessageChannel userOrderPaymentConfirmationChannel();

    @Output(ADVERTISING)
    MessageChannel advertisingChannel();

    @Input(EXTERNAL_PRODUCT_INPUT)
    SubscribableChannel externalProductInputChannel();

    @Output(EXTERNAL_PRODUCT)
    MessageChannel externalProductChannel();

}

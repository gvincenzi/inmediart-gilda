package org.inmediart.mail.listener;

import org.inmediart.mail.binding.GassmanMessage;
import org.inmediart.mail.binding.MQBinding;
import org.inmediart.mail.dto.AdvertisingDTO;
import org.inmediart.mail.service.MailService;
import org.inmediart.mail.dto.OrderDTO;
import org.inmediart.mail.dto.RechargeUserCreditLogDTO;
import org.inmediart.mail.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    MailService mailService;

    @Value("${gassman.instance.id}")
    private String instanceId;

    @Value("${gassman.instance.botName}")
    private String botName;

    @StreamListener(target = MQBinding.USER_REGISTRATION)
    public void processUserRegistration(GassmanMessage<UserDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendRegistrationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.USER_CANCELLATION)
    public void processUserCancellation(GassmanMessage<UserDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendUserCancellationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.USER_ORDER)
    public void processOrder(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendOrderMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.ORDER_UPDATE)
    public void processOrderUpdate(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendOrderUpdateMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.RECHARGE_USER_CREDIT)
    public void processRechargeUserCredit(GassmanMessage<RechargeUserCreditLogDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendRechargeUserCreditMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.ORDER_PAYMENT_CONFIRMATION)
    public void processOrderPaymentConfirmation(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendOrderPaymentConfirmationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.ORDER_CANCELLATION)
    public void processOrderCancellation(GassmanMessage<OrderDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendOrderCancellationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.ADVERTISING)
    public void processAdvertising(GassmanMessage<AdvertisingDTO> msg) {
        if(checkInstance(msg)){
            mailService.sendAdvertisingMessage(msg.getPayload());
        }
    }

    private boolean checkInstance(GassmanMessage msg) {
        return msg.getParams() != null && msg.getParams().length>=2
                && instanceId.equalsIgnoreCase(msg.getParams()[0])
                && botName.equalsIgnoreCase(msg.getParams()[1]);
    }
}

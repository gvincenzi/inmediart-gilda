package org.inmediart.telegram.bot.listener;

import org.inmediart.commons.binding.GassmanMessage;
import org.inmediart.commons.binding.MQBinding;
import org.inmediart.telegram.bot.dto.OrderDTO;
import org.inmediart.telegram.bot.dto.UserDTO;
import org.inmediart.telegram.bot.service.TelegramAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EnableBinding(MQBinding.class)
public class MQListener {
    @Autowired
    TelegramAdministratorService telegramAdministratorService;

    @Value("${gassman.instance.id}")
    private String instanceId;

    @Value("${gassman.instance.botName}")
    private String botName;

    @StreamListener(target = MQBinding.USER_REGISTRATION)
    public void processUserRegistration(GassmanMessage<UserDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendRegistrationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.USER_ORDER)
    public void processUserOrder(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.ORDER_UPDATE)
    public void processOrderUpdate(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderUpdateMessage(msg.getPayload());
        }
    }

    @StreamListener(target = MQBinding.ORDER_CANCELLATION)
    public void processOrderCancellation(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderCancellationMessage(msg.getPayload());
        }
    }

    private boolean checkInstance(GassmanMessage msg) {
        return msg.getParams() != null && msg.getParams().length>=2
                && instanceId.equalsIgnoreCase(msg.getParams()[0])
                && botName.equalsIgnoreCase(msg.getParams()[1]);
    }
}
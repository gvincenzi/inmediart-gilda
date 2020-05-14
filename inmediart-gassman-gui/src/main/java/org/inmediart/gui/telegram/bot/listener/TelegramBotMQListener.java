package org.inmediart.gui.telegram.bot.listener;

import org.inmediart.gui.binding.GUIMQBinding;
import org.inmediart.commons.messaging.GassmanMessage;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.UserDTO;
import org.inmediart.gui.telegram.bot.service.TelegramAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EnableBinding(GUIMQBinding.class)
public class TelegramBotMQListener {
    @Autowired
    TelegramAdministratorService telegramAdministratorService;

    @Value("${gassman.instance.id}")
    private String instanceId;

    @Value("${gassman.instance.botName}")
    private String botName;

    @StreamListener(target = GUIMQBinding.USER_REGISTRATION)
    public void processUserRegistration(GassmanMessage<UserDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendRegistrationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = GUIMQBinding.USER_ORDER)
    public void processUserOrder(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderMessage(msg.getPayload());
        }
    }

    @StreamListener(target = GUIMQBinding.ORDER_UPDATE)
    public void processOrderUpdate(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderUpdateMessage(msg.getPayload());
        }
    }

    @StreamListener(target = GUIMQBinding.ORDER_CANCELLATION)
    public void processOrderCancellation(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderCancellationMessage(msg.getPayload());
        }
    }

    @StreamListener(target = GUIMQBinding.PRODUCT_UPDATE)
    public void processProductUpdate(GassmanMessage<OrderDTO> msg) throws TelegramApiException {
        if(checkInstance(msg)){
            telegramAdministratorService.sendOrderUpdateMessage(msg.getPayload());
        }
    }

    private boolean checkInstance(GassmanMessage msg) {
        return msg.getParams() != null && msg.getParams().length>=2
                && instanceId.equalsIgnoreCase(msg.getParams()[0])
                && botName.equalsIgnoreCase(msg.getParams()[1]);
    }
}
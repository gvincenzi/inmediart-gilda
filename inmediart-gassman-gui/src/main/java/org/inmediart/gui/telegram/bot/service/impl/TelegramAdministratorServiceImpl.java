package org.inmediart.gui.telegram.bot.service.impl;

import org.inmediart.gui.telegram.bot.polling.InmediartOrderBot;
import org.inmediart.gui.client.UserResourceClient;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.UserDTO;
import org.inmediart.gui.telegram.bot.service.TelegramAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
public class TelegramAdministratorServiceImpl implements TelegramAdministratorService {
    @Autowired
    InmediartOrderBot inmediartOrderBot;

    @Autowired
    UserResourceClient userResourceClient;

    @Override
    public void sendRegistrationMessage(UserDTO userDTO) throws TelegramApiException {
        List<UserDTO> administrators = userResourceClient.getAdministrators();
        if(administrators != null && !administrators.isEmpty()) {
            for(UserDTO administrator : administrators) {
                SendMessage message = new SendMessage()
                        .setChatId(String.valueOf(administrator.getTelegramUserId()))
                        .setText("Nuovo utente registrato : " + userDTO.getName() + " " + userDTO.getSurname());
                inmediartOrderBot.execute(message);
            }
        }
    }

    @Override
    public void sendOrderMessage(OrderDTO orderDTO) throws TelegramApiException {
        List<UserDTO> administrators = userResourceClient.getAdministrators();
        if(administrators != null && !administrators.isEmpty()) {
            for(UserDTO administrator : administrators) {
                SendMessage message = new SendMessage()
                        .setChatId(String.valueOf(administrator.getTelegramUserId()))
                        .setText("Nuovo ordine registrato da "+orderDTO.getUser().getName()+" "+orderDTO.getUser().getSurname()+":\n" + orderDTO.toString());
                inmediartOrderBot.execute(message);
            }
        }
    }

    @Override
    public void sendOrderUpdateMessage(OrderDTO msg) throws TelegramApiException {
        UserDTO destination = userResourceClient.findUserByTelegramId(msg.getUser().getTelegramUserId());
        if(destination != null) {
            SendMessage message = new SendMessage()
                    .setChatId(String.valueOf(destination.getTelegramUserId()))
                    .setText("Aggiornamento del tuo ordine #"+msg.getOrderId() + ":\n" + msg.toString() + "\nClicca su /start per tornare al menu principale.");
            inmediartOrderBot.execute(message);
        }
    }

    @Override
    public void sendOrderCancellationMessage(OrderDTO msg) throws TelegramApiException {
        UserDTO destination = userResourceClient.findUserByTelegramId(msg.getUser().getTelegramUserId());
        if(destination != null) {
            SendMessage message = new SendMessage()
                    .setChatId(String.valueOf(destination.getTelegramUserId()))
                    .setText("Annullamento del tuo ordine #"+msg.getOrderId() + ":\n" + msg.toString() + "\nClicca su /start per tornare al menu principale.");
            inmediartOrderBot.execute(message);
        }

        List<UserDTO> administrators = userResourceClient.getAdministrators();
        if(administrators != null && !administrators.isEmpty()) {
            for(UserDTO administrator : administrators) {
                SendMessage message = new SendMessage()
                        .setChatId(String.valueOf(administrator.getTelegramUserId()))
                        .setText("Annullamento di un ordine di "+msg.getUser().getName()+" "+msg.getUser().getSurname()+":\n" + msg.toString());
                inmediartOrderBot.execute(message);
            }
        }
    }
}

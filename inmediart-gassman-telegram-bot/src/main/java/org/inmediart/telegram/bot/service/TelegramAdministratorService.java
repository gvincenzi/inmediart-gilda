package org.inmediart.telegram.bot.service;

import org.inmediart.telegram.bot.dto.OrderDTO;
import org.inmediart.telegram.bot.dto.UserDTO;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramAdministratorService {
    void sendRegistrationMessage(UserDTO userDTO) throws TelegramApiException;
    void sendOrderMessage(OrderDTO orderDTO) throws TelegramApiException;
    void sendOrderUpdateMessage(OrderDTO msg) throws TelegramApiException;
    void sendOrderCancellationMessage(OrderDTO msg) throws TelegramApiException;
}

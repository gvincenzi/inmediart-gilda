package org.inmediart.gui.telegram.bot.polling.factory;

import org.inmediart.gui.dto.UserDTO;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ItemFactory {
    SendMessage welcomeMessage(Update update);
    SendMessage message(Long chat_id, String text);
    SendMessage userManagementMenu(Long chat_id, UserDTO userToManage);
    SendMessage credit(Long chat_id);
    SendMessage userSearch(Long chat_id);
    SendMessage userManagementCredit(Long chat_id);
    SendMessage selectProductQuantity(Long chat_id);
    SendMessage selectAddress(Long chat_id);
}

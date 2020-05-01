package org.inmediart.telegram.bot.polling.factory;

import org.inmediart.telegram.bot.dto.UserDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ItemFactory {
    SendMessage welcomeMessage(Update update);
    SendMessage message(Long chat_id, String text);
    SendMessage userManagementMenu(Long chat_id, UserDTO userToManage);
    SendMessage credit(Long chat_id);
    SendMessage userSearch(Long chat_id);
    SendMessage userManagementCredit(Long chat_id);
}

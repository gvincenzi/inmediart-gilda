package org.inmediart.gui.telegram.bot.polling.factory.impl;

import org.apache.commons.lang3.StringUtils;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.ProductDTO;
import org.inmediart.gui.dto.UserDTO;
import org.inmediart.gui.telegram.bot.polling.factory.ItemFactory;
import org.inmediart.gui.telegram.bot.service.ResourceManagerService;
import org.inmediart.model.entity.Action;
import org.inmediart.model.entity.type.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemFactoryImpl implements ItemFactory {
    @Autowired
    ResourceManagerService resourceManagerService;

    public SendMessage welcomeMessage(Message update, Integer user_id) {
        SendMessage message;
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        UserDTO user = resourceManagerService.findUserByTelegramId(user_id);
        if(user != null) {
            Action actionInProgress = resourceManagerService.getActionInProgress(user.getTelegramUserId());
            if (actionInProgress != null && ActionType.USER_SEARCH.equals(actionInProgress.getActionType())) {
                resourceManagerService.deleteActionInProgress(actionInProgress);
                UserDTO userByMail = resourceManagerService.getUserByMail(update.getText());
                if (userByMail == null) {
                    return message(update.getChatId(),"Nessun iscritto con questa mail\nClicca su /start per tornare al menu principale.");
                } else {
                    Action action = new Action();
                    action.setActionType(ActionType.USER_MANAGEMENT);
                    action.setTelegramUserIdToManage(userByMail.getTelegramUserId());
                    action.setTelegramUserId(user.getTelegramUserId());
                    resourceManagerService.saveAction(action);
                    return userManagementMenu(update.getChatId(), userByMail);
                }

            } else if (actionInProgress != null && ActionType.USER_MANAGEMENT.equals(actionInProgress.getActionType())) {
                UserDTO userToManage = resourceManagerService.findUserByTelegramId(actionInProgress.getTelegramUserIdToManage());
                if (userToManage != null) {
                    return userManagementMenu(update.getChatId(), userToManage);
                }
            } else if (actionInProgress != null && ActionType.USER_CREDIT.equals(actionInProgress.getActionType())) {
                UserDTO userToManage = resourceManagerService.findUserByTelegramId(actionInProgress.getTelegramUserIdToManage());
                if (userToManage != null) {
                    return userManagementCredit(update.getChatId());
                }
            } else if (actionInProgress != null && ActionType.SELECT_PRODUCT.equals(actionInProgress.getActionType())) {
                return selectProductQuantity(update.getChatId());
            } else if (actionInProgress != null && ActionType.PRODUCT_URL.equals(actionInProgress.getActionType())) {
                ProductDTO product = resourceManagerService.getProductById(actionInProgress.getProductIdToManage());
                resourceManagerService.deleteActionInProgress(actionInProgress);
                if (product == null) {
                    return message(update.getChatId(),"Nessun prodotto con questo ID in catalogo\nClicca su /start per tornare al menu principale.");
                } else {
                    product.setUrl(update.getText());
                    resourceManagerService.updateProduct(product);
                    return message(update.getChatId(),"Modifica terminata.\nClicca su /start per tornare al menu principale.");
                }
            }
        }

        message = new SendMessage()
                .setChatId(update.getChatId())
                .setText(String.format("%s,\nScegli tra le seguenti opzioni:", user == null ? "Benvenuto nel sistema InMediArt GasSMan" : "Ciao " + user.getName()));

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline5 = new ArrayList<>();
        if (user == null) {
            rowInline1.add(new InlineKeyboardButton().setText("Iscrizione").setCallbackData("iscrizione"));
        } else {
            rowInline1.add(new InlineKeyboardButton().setText("Catalogo").setCallbackData("catalogo"));
            rowInline2.add(new InlineKeyboardButton().setText("I tuoi ordini").setCallbackData("listaOrdini"));
            rowInline3.add(new InlineKeyboardButton().setText("Credito residuo").setCallbackData("creditoResiduo"));
            rowInline3.add(new InlineKeyboardButton().setText("Ricarica credito").setCallbackData("ricaricaCredito"));
            rowInline4.add(new InlineKeyboardButton().setText("Cancellazione").setCallbackData("cancellazione"));
            rowInline5.add(new InlineKeyboardButton().setText("Gestione iscritti").setCallbackData("usermng"));
        }

        // Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        // FIXME Can an user delete its own account ? -- rowsInline.add(rowInline4);
        if (user != null && user.getAdministrator()) {
            rowsInline.add(rowInline5);
        }

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    @Override
    public SendMessage message(Long chat_id, String text) {
        return new SendMessage()
                .setChatId(chat_id)
                .setText(text);
    }

    @Override
    public SendMessage userSearch(Long chat_id) {
        SendMessage message;

        message = new SendMessage()
                .setChatId(chat_id)
                .setText(String.format("Scrivi la mail dell'utente che vuoi gestire"));

        return message;
    }

    @Override
    public SendMessage userManagementMenu(Long chat_id, UserDTO userToManage) {
        SendMessage message;
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        message = new SendMessage()
                .setChatId(chat_id)
                .setText(String.format("Utente : %s %s\nCredito residuo : %s €", userToManage.getName(), userToManage.getSurname(), resourceManagerService.getCredit(userToManage.getTelegramUserId()).getCredit()));
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("Ricarica credito").setCallbackData("usermng#ricaricaCredito"));
        rowInline1.add(new InlineKeyboardButton().setText("Cancellazione").setCallbackData("usermng#cancellazione"));
        rowInline2.add(new InlineKeyboardButton().setText("Modifica terminata").setCallbackData("usermng#end"));

        // Set the keyboard to the markup
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    @Override
    public SendMessage credit(Long chat_id) {
        SendMessage message;
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        message = new SendMessage()
                .setChatId(chat_id)
                .setText("Ricarica il tuo credito scegliendo tra le seguenti opzioni:");

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("5 euro").setCallbackData("credit#500"));
        rowInline1.add(new InlineKeyboardButton().setText("10 euro").setCallbackData("credit#1000"));
        rowInline2.add(new InlineKeyboardButton().setText("20 euro").setCallbackData("credit#2000"));
        rowInline2.add(new InlineKeyboardButton().setText("50 euro").setCallbackData("credit#5000"));

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    @Override
    public SendMessage userManagementCredit(Long chat_id) {
        SendMessage message;
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        message = new SendMessage()
                .setChatId(chat_id)
                .setText("Ricarica il credito dell'utente scegliendo tra le seguenti opzioni:");

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline1.add(new InlineKeyboardButton().setText("5 euro").setCallbackData("usermng#ricaricaCredito#500"));
        rowInline1.add(new InlineKeyboardButton().setText("10 euro").setCallbackData("usermng#ricaricaCredito#1000"));
        rowInline2.add(new InlineKeyboardButton().setText("20 euro").setCallbackData("usermng#ricaricaCredito#2000"));
        rowInline2.add(new InlineKeyboardButton().setText("50 euro").setCallbackData("usermng#ricaricaCredito#5000"));

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message;
    }

    @Override
    public SendMessage selectProductQuantity(Long chat_id) {
        return message(chat_id, "Inviare ora un messaggio indicando la quantità desiderata (solo il valore numerico) per finalizzare l'ordine");
    }

    @Override
    public SendMessage selectAddress(Long chat_id) {
        return message(chat_id, "Inviare un ulteriore messaggio indicando l'indirizzo di spedizione per finalizzare l'ordine");
    }

    @Override
    public SendMessage productUrlManagement(Long chat_id) {
        return message(chat_id, "Inviare un ulteriore messaggio indicando l'URL da associare al prodotto");
    }

    @Override
    public void orderDetailsMessageBuilder(SendMessage message, OrderDTO orderDTO) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline5 = new ArrayList<>();

        rowInline1.add(new InlineKeyboardButton().setText("Paga questo ordine : "+ NumberFormat.getCurrencyInstance().format(orderDTO.getAmount())).setCallbackData("makePayment#"+orderDTO.getOrderId()));
        rowInline2.add(new InlineKeyboardButton().setText("Annulla questo ordine").setCallbackData("deleteOrder#"+orderDTO.getOrderId()));
        if(StringUtils.isNotEmpty(orderDTO.getProduct().getUrl())){
            rowInline3.add(new InlineKeyboardButton().setText("Guarda il contenuto").setUrl(orderDTO.getProduct().getUrl()));
        }
        rowInline4.add(new InlineKeyboardButton().setText("Torna alla lista degli ordini").setCallbackData("listaOrdini"));
        rowInline5.add(new InlineKeyboardButton().setText("Torna al menù principale").setCallbackData("welcomeMenu"));
        // Set the keyboard to the markup
        if(!orderDTO.getPaid()){
            rowsInline.add(rowInline1);
            rowsInline.add(rowInline2);
        } else {
            if(StringUtils.isNotEmpty(orderDTO.getProduct().getUrl())){
                rowsInline.add(rowInline3);
            }
        }

        rowsInline.add(rowInline4);
        rowsInline.add(rowInline5);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
    }
}

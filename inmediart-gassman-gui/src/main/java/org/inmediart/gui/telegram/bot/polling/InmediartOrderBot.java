package org.inmediart.gui.telegram.bot.polling;

import org.apache.commons.lang3.StringUtils;
import org.inmediart.model.entity.Action;
import org.inmediart.model.entity.type.ActionType;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.ProductDTO;
import org.inmediart.gui.dto.UserDTO;
import org.inmediart.gui.telegram.bot.polling.factory.ItemFactory;
import org.inmediart.gui.telegram.bot.service.ResourceManagerService;
import org.inmediart.gui.telegram.bot.service.TelegramAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InmediartOrderBot extends TelegramLongPollingBot {
    @Value("${inmediart.telegram.bot.username}")
    private String botUsername;

    @Value("${inmediart.telegram.bot.token}")
    private String botToken;

    @Value("${inmediart.telegram.bot.stripe.token}")
    private String stripeToken;

    @Autowired
    ResourceManagerService resourceManagerService;

    @Autowired
    TelegramAdministratorService telegramAdministratorService;

    @Autowired
    ItemFactory itemFactory;

    @Override
    public void onUpdateReceived(Update update) {
        BotApiMethod message = null;
        Integer user_id = null;

        if(update.hasPreCheckoutQuery()){
            /* CHECK PAYLOAD */
            message = new AnswerPreCheckoutQuery();
            ((AnswerPreCheckoutQuery)message).setOk(true);
            ((AnswerPreCheckoutQuery)message).setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
        }
        if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            user_id = update.getCallbackQuery().getFrom().getId();
            Long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("iscrizione")) {
                message = itemFactory.message(chat_id, "Per iscriversi al sistema basta scrivere un messaggio in questa chat con solo la propria email.\nInMediArt GasSMan vi iscriverà al sistema con i dati del vostro account Telegram e con la mail che avrete indicato");
            } else if (call_data.equals("cancellazione")) {
                resourceManagerService.deleteUser(user_id);
                message = itemFactory.message(chat_id, "Utente rimosso correttamente");
            } else if (call_data.equals("creditoResiduo")) {
                message = itemFactory.message(chat_id,String.format("Il tuo credito residuo : %s €", resourceManagerService.getCredit(user_id).getCredit()));
            } else if (call_data.equals("ricaricaCredito")) {
                message = itemFactory.credit(chat_id);
            } else if(call_data.startsWith("credit#")) {
                String choice = call_data.substring(call_data.indexOf("#") + 1);
                StringBuilder payload = new StringBuilder();
                payload.append(user_id);
                payload.append(choice);
                LabeledPrice price = new LabeledPrice();
                price.setLabel("Ricarica credito");
                price.setAmount(Integer.parseInt(choice));

                message = new SendInvoice();
                ((SendInvoice) message).setProviderToken(stripeToken);
                List<LabeledPrice> prices = new ArrayList<>();
                prices.add(price);
                ((SendInvoice) message).setPrices(prices);
                ((SendInvoice) message).setTitle("InMediArt GasSMan - Credito");
                ((SendInvoice) message).setDescription("Ricarica del conto prepagato");
                ((SendInvoice) message).setCurrency("EUR");
                ((SendInvoice) message).setChatId(chat_id.intValue());
                ((SendInvoice) message).setPayload(payload.toString());
                ((SendInvoice) message).setStartParameter("pay");

            } else if (call_data.startsWith("listaOrdini")) {
                List<OrderDTO> orders = resourceManagerService.getOrders(user_id);
                if (orders.isEmpty()) {
                    message = itemFactory.message(chat_id,"Non hai ordini in corso");
                } else {
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    Collections.sort(orders);
                    for (OrderDTO orderDTO : orders) {
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();
                        rowInline.add(new InlineKeyboardButton().setText("ID#"+orderDTO.getOrderId()+" : "+orderDTO.getProduct().getName()).setCallbackData("orderDetails#" + orderDTO.getOrderId()));
                        rowsInline.add(rowInline);
                    }

                    markupInline.setKeyboard(rowsInline);
                    message = itemFactory.message(chat_id,"Qui di seguito la lista dei tuoi ordini in corso, per accedere ai dettagli cliccare sull'ordine:\n");

                    ((SendMessage)message).setReplyMarkup(markupInline);
                }
            } else if (call_data.startsWith("orderDetails#")) {
                OrderDTO orderDTO = resourceManagerService.getOrder(call_data);
                message = itemFactory.message(chat_id,orderDTO.toString());
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
                List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
                List<InlineKeyboardButton> rowInline4 = new ArrayList<>();

                rowInline1.add(new InlineKeyboardButton().setText("Paga questo ordine : "+ NumberFormat.getCurrencyInstance().format(orderDTO.getAmount())).setCallbackData("makePayment#"+orderDTO.getOrderId()));
                rowInline2.add(new InlineKeyboardButton().setText("Annulla questo ordine").setCallbackData("deleteOrder#"+orderDTO.getOrderId()));
                rowInline3.add(new InlineKeyboardButton().setText("Guarda il contenuto").setUrl(orderDTO.getProduct().getUrl()));
                rowInline4.add(new InlineKeyboardButton().setText("Torna alla lista").setCallbackData("listaOrdini"));
                // Set the keyboard to the markup
                if(!orderDTO.getPaid()){
                    rowsInline.add(rowInline1);
                    rowsInline.add(rowInline2);
                } else {
                    rowsInline.add(rowInline3);
                }

                // rowInline1.
                rowsInline.add(rowInline4);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                ((SendMessage)message).setReplyMarkup(markupInline);
            } else if (call_data.startsWith("makePayment#")) {
                OrderDTO orderDTO = resourceManagerService.getOrder(call_data);
                message = itemFactory.message(chat_id, resourceManagerService.makePayment(orderDTO) + "\nClicca su /start per tornare al menu principale.");
            } else if (call_data.startsWith("deleteOrder#")) {
                OrderDTO orderDTO = resourceManagerService.getOrder(call_data);
                if(orderDTO.getPaid()){
                    message = itemFactory.message(chat_id, "Non e' più possibile annullare questo ordine.");
                } else {
                    resourceManagerService.deleteOrder(orderDTO);
                    message = itemFactory.message(chat_id, "Richiesta dell'annullamento inviata con successo. Riceverai una notifica su Telegram e una mail di conferma.");
                }
            } else if (call_data.startsWith("catalogo")) {
                List<ProductDTO> products = resourceManagerService.getProducts();
                if (products.isEmpty()) {
                    message = itemFactory.message(chat_id,"Non ci sono elementi nel catalogo");
                } else {
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    Collections.sort(products);
                    for (ProductDTO productDTO : products) {
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();
                        rowInline.add(new InlineKeyboardButton().setText(productDTO.getName()).setCallbackData("detailProduct#" + productDTO.getProductId()));
                        rowsInline.add(rowInline);
                    }

                    markupInline.setKeyboard(rowsInline);
                    message = itemFactory.message(chat_id,"Qui di seguito la lista dei documenti in catalogo, selezionane uno per ordinarlo :\n");

                    ((SendMessage)message).setReplyMarkup(markupInline);
                }
            } else if (call_data.startsWith("detailProduct#")) {
                    ProductDTO productDTO = resourceManagerService.getProduct(call_data);
                    message = itemFactory.message(chat_id,productDTO.toString());
                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("Ordina questo prodotto").setCallbackData("selectProduct#" + productDTO.getProductId()));
                    rowInline.add(new InlineKeyboardButton().setText("Torna alla lista").setCallbackData("listaProdotti"));
                    // Set the keyboard to the markup
                    rowsInline.add(rowInline);
                    // Add it to the message
                    markupInline.setKeyboard(rowsInline);
                    ((SendMessage)message).setReplyMarkup(markupInline);
            } else if (call_data.startsWith("selectProduct#")) {
                ProductDTO productDTO = resourceManagerService.getProduct(call_data);

                if(productDTO.getAvailableQuantity() == null) {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setActionType(ActionType.BUY);
                    UserDTO userDTO = new UserDTO();
                    userDTO.setTelegramUserId(user_id);
                    orderDTO.setUser(userDTO);
                    orderDTO.setProduct(productDTO);

                    message = itemFactory.message(chat_id, resourceManagerService.postOrder(orderDTO));
                } else {
                    Action action = new Action();
                    action.setActionType(ActionType.SELECT_PRODUCT);
                    action.setTelegramUserId(user_id);
                    action.setSelectedProductId(productDTO.getProductId());
                    resourceManagerService.saveAction(action);
                    message = itemFactory.selectProductQuantity(chat_id);
                }
            } else if (call_data.equalsIgnoreCase("usermng")) {
                Action action = new Action();
                action.setActionType(ActionType.USER_SEARCH);
                action.setTelegramUserId(user_id);
                resourceManagerService.saveAction(action);
                message = itemFactory.userSearch(chat_id);
            } else if (call_data.equalsIgnoreCase("usermng#end")) {
                Action actionInProgress = getActionInProgress(user_id);
                if(actionInProgress != null && actionInProgress.getTelegramUserIdToManage() != null) {
                    resourceManagerService.deleteActionInProgress(actionInProgress);
                    message = itemFactory.message(chat_id,"Modifica terminata.\nClicca su /start per tornare al menu principale.");
                }
            } else if (call_data.equalsIgnoreCase("usermng#cancellazione")) {
                Action actionInProgress = getActionInProgress(user_id);
                if(actionInProgress != null && actionInProgress.getTelegramUserIdToManage() != null) {
                    resourceManagerService.deleteUser(actionInProgress.getTelegramUserIdToManage());
                    resourceManagerService.deleteActionInProgress(actionInProgress);
                    message = itemFactory.message(chat_id, "Utente rimosso correttamente\nClicca su /start per tornare al menu principale.");
                }
            } else if (call_data.equalsIgnoreCase("usermng#ricaricaCredito")) {
                Action actionInProgress = getActionInProgress(user_id);
                if(actionInProgress != null && actionInProgress.getTelegramUserIdToManage() != null) {
                    Action action = new Action();
                    action.setActionType(ActionType.USER_CREDIT);
                    action.setTelegramUserIdToManage(actionInProgress.getTelegramUserIdToManage());
                    action.setTelegramUserId(user_id);
                    resourceManagerService.deleteActionInProgress(actionInProgress);
                    resourceManagerService.saveAction(action);
                    message = itemFactory.userManagementCredit(chat_id);
                }
            } else if (call_data.startsWith("usermng#ricaricaCredito#")) {
                String[] split = call_data.split("#");
                Long credit = Long.parseLong(split[2]);
                Action actionInProgress = getActionInProgress(user_id);
                if(actionInProgress != null && actionInProgress.getTelegramUserIdToManage() != null && ActionType.USER_CREDIT.equals(actionInProgress.getActionType())) {
                    resourceManagerService.deleteActionInProgress(actionInProgress);
                    resourceManagerService.addCredit(actionInProgress.getTelegramUserIdToManage(), BigDecimal.valueOf(credit));
                    message = itemFactory.message(chat_id, "Credito aggiornato correttamente\nClicca su /start per tornare al menu principale.");
                }
            }
        } else if (update.hasMessage()){
            user_id = update.getMessage().getFrom().getId();
            Long chat_id = update.getMessage().getChatId();
            Action actionInProgress = getActionInProgress(user_id);

            if (update.getMessage().getText() != null && update.getMessage().getText().equalsIgnoreCase("/start")) {
                message = itemFactory.welcomeMessage(update);
            } else if (update.getMessage().getText() != null && update.getMessage().getText().contains("@") && actionInProgress ==null) {
                resourceManagerService.addUser(update.getMessage().getFrom(), update.getMessage().getText());
                message = itemFactory.message(chat_id, "Nuovo utente iscritto correttamente : una mail di conferma è stata inviata all'indirizzo specificato.\nClicca su /start per iniziare.");
            } else if (update.getMessage().getText() != null && StringUtils.isNumeric(update.getMessage().getText()) && actionInProgress !=null && ActionType.SELECT_PRODUCT.equals(actionInProgress.getActionType())) {
                Action action = new Action();
                action.setActionType(ActionType.SELECT_ADDRESS);
                action.setTelegramUserId(user_id);
                action.setSelectedProductId(actionInProgress.getSelectedProductId());
                action.setQuantity(Double.parseDouble(update.getMessage().getText()));
                resourceManagerService.saveAction(action);
                resourceManagerService.deleteActionInProgress(actionInProgress);
                message = itemFactory.selectAddress(chat_id);
            } else if (update.getMessage().getText() != null && !StringUtils.isNumeric(update.getMessage().getText()) && actionInProgress !=null && ActionType.SELECT_ADDRESS.equals(actionInProgress.getActionType())) {
                resourceManagerService.deleteActionInProgress(actionInProgress);
                ProductDTO productDTO = resourceManagerService.getProductById(actionInProgress.getSelectedProductId());
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setActionType(ActionType.BUY_PHISICAL);
                UserDTO userDTO = new UserDTO();
                userDTO.setTelegramUserId(user_id);
                orderDTO.setUser(userDTO);
                orderDTO.setProduct(productDTO);
                orderDTO.setQuantity(actionInProgress.getQuantity());
                orderDTO.setAddress(update.getMessage().getText());

                message = itemFactory.message(chat_id, resourceManagerService.postOrder(orderDTO));
            } else if (update.hasMessage()) {
                message = itemFactory.welcomeMessage(update);
            }
        }

        try {
            execute(message); // Call method to send the message

            if(message instanceof AnswerPreCheckoutQuery){
                resourceManagerService.addCredit(update.getPreCheckoutQuery().getFrom().getId(),BigDecimal.valueOf(update.getPreCheckoutQuery().getTotalAmount()));
            }
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private Action getActionInProgress(Integer user_id) {
        return resourceManagerService.getActionInProgress(user_id);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

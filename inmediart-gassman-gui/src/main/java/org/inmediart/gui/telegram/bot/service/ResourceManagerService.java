package org.inmediart.gui.telegram.bot.service;

import org.inmediart.model.entity.Action;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.ProductDTO;
import org.inmediart.gui.dto.UserCreditDTO;
import org.inmediart.gui.dto.UserDTO;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigDecimal;
import java.util.List;

public interface ResourceManagerService {
    void deleteUser(Integer user_id);
    UserDTO findUserByTelegramId(Integer user_id);
    void addUser(User user, String mail);
    Action getActionInProgress(Integer telegramUserId);
    void deleteActionInProgress(Action action);
    void saveAction(Action action);
    String postOrder(OrderDTO orderDTO);
    OrderDTO getOrder(String call_data);
    List<OrderDTO> getOrders(Integer user_id);
    UserCreditDTO getCredit(Integer user_id);
    UserCreditDTO addCredit(Integer user_id, BigDecimal credit);
    String makePayment(Long orderId);
    void deleteOrder(OrderDTO orderDTO);
    List<ProductDTO> getProducts();
    ProductDTO getProduct(String call_data);
    ProductDTO getProductById(Long productId);
    UserDTO getUserByMail(String call_data);
}

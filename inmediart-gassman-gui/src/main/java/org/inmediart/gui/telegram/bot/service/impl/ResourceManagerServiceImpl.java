package org.inmediart.gui.telegram.bot.service.impl;

import feign.FeignException;
import org.inmediart.model.entity.Action;
import org.inmediart.model.repository.ActionRepository;
import org.inmediart.gui.client.OrderResourceClient;
import org.inmediart.gui.client.ProductResourceClient;
import org.inmediart.gui.client.UserCreditResourceClient;
import org.inmediart.gui.client.UserResourceClient;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.ProductDTO;
import org.inmediart.gui.dto.UserCreditDTO;
import org.inmediart.gui.dto.UserDTO;
import org.inmediart.gui.telegram.bot.polling.InmediartOrderBot;
import org.inmediart.gui.telegram.bot.service.ResourceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ResourceManagerServiceImpl implements ResourceManagerService {
    @Autowired
    InmediartOrderBot inmediartOrderBot;

    @Autowired
    private UserResourceClient userResourceClient;

    @Autowired
    private OrderResourceClient orderResourceClient;

    @Autowired
    private ProductResourceClient productResourceClient;

    @Autowired
    private UserCreditResourceClient userCreditResourceClient;

    @Autowired
    private ActionRepository actionRepository;

    public UserDTO findUserByTelegramId(Integer user_id) {
        UserDTO user;
        try {
            user = userResourceClient.findUserByTelegramId(user_id);
        } catch (FeignException ex) {
            System.out.println(ex.getMessage());
            user = null;
        }
        return user;
    }

    @Override
    public void addUser(User from, String mail) {
        UserDTO userDTO = new UserDTO();
        userDTO.setTelegramUserId(from.getId());
        userDTO.setName(from.getFirstName());
        userDTO.setSurname(from.getLastName());
        userDTO.setMail(mail);
        userDTO.setAdministrator(Boolean.FALSE);
        userResourceClient.addUser(userDTO);
    }

    @Override
    public void deleteUser(Integer user_id) {
        userResourceClient.deleteUser(user_id);
    }

    @Override
    public Action getActionInProgress(Integer telegramUserId){
        Optional<Action> actionOptional = actionRepository.findByTelegramUserIdAndInProgressTrue(telegramUserId);
        if(actionOptional.isPresent()){
            return actionOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public void deleteActionInProgress(Action action) {
        actionRepository.delete(action);
    }

    @Override
    public void saveAction(Action action) {
        actionRepository.save(action);
    }

    @Override
    public String postOrder(OrderDTO orderDTO) {
        return orderResourceClient.postOrder(orderDTO) + "\nClicca su /start per tornare al menu principale.";
    }

    @Override
    public List<OrderDTO> getOrders(Integer user_id) {
        UserDTO userDTO = findUserByTelegramId(user_id);
        return orderResourceClient.findAllOrdersByUser(userDTO.getId());
    }

    @Override
    public OrderDTO getOrder(String call_data) {
        String[] split = call_data.split("#");
        Long orderId = Long.parseLong(split[1]);
        OrderDTO orderDTO = orderResourceClient.findOrderById(orderId);
        return orderDTO;
    }

    @Override
    public UserCreditDTO getCredit(Integer user_id) {
        UserDTO user = findUserByTelegramId(user_id);
        return userCreditResourceClient.findById(user.getId());
    }

    @Override
    public UserCreditDTO addCredit(Integer user_id, BigDecimal credit) {
        UserDTO user = findUserByTelegramId(user_id);
        return userCreditResourceClient.addCredit(user, credit.divide(BigDecimal.valueOf(100)));
    }

    @Override
    public String makePayment(OrderDTO orderDTO) {
        return userCreditResourceClient.makePayment(orderDTO.getOrderId());
    }

    @Override
    public void deleteOrder(OrderDTO orderDTO) {
        orderResourceClient.deleteOrder(orderDTO.getOrderId());
    }

    @Override
    public List<ProductDTO> getProducts() {
        return productResourceClient.findActives();
    }

    @Override
    public ProductDTO getProduct(String call_data) {
        String[] split = call_data.split("#");
        Long productId = Long.parseLong(split[1]);
        return productResourceClient.findProductById(productId);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        return productResourceClient.findById(productId);
    }

    @Override
    public UserDTO getUserByMail(String call_data) {
        return userResourceClient.findUserByMail(call_data);
    }
}

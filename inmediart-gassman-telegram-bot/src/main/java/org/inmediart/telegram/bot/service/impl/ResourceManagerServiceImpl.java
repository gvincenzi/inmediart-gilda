package org.inmediart.telegram.bot.service.impl;

import feign.FeignException;
import org.apache.commons.io.FileUtils;
import org.inmediart.telegram.bot.client.OrderResourceClient;
import org.inmediart.telegram.bot.client.ProductResourceClient;
import org.inmediart.telegram.bot.client.UserCreditResourceClient;
import org.inmediart.telegram.bot.polling.InmediartOrderBot;
import org.inmediart.telegram.bot.client.UserResourceClient;
import org.inmediart.telegram.bot.dto.OrderDTO;
import org.inmediart.telegram.bot.dto.ProductDTO;
import org.inmediart.telegram.bot.dto.UserCreditDTO;
import org.inmediart.telegram.bot.dto.UserDTO;
import org.inmediart.telegram.bot.model.Action;
import org.inmediart.telegram.bot.model.type.ActionType;
import org.inmediart.telegram.bot.repository.ActionRepository;
import org.inmediart.telegram.bot.service.ResourceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
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
    public void postOrder(OrderDTO orderDTO) {
        orderResourceClient.postOrder(orderDTO);
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
        return productResourceClient.findAll();
    }

    @Override
    public ProductDTO getProduct(String call_data) {
        String[] split = call_data.split("#");
        Long productId = Long.parseLong(split[1]);
        return productResourceClient.findProductById(productId);
    }

    @Override
    public UserDTO getUserByMail(String call_data) {
        return userResourceClient.findUserByMail(call_data);
    }
}

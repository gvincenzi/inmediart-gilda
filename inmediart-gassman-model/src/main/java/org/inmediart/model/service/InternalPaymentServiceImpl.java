package org.inmediart.model.service;

import org.inmediart.commons.exception.GassmanAvailableQuantityException;
import org.inmediart.commons.messaging.MessageSender;
import org.inmediart.model.entity.*;
import org.inmediart.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InternalPaymentServiceImpl extends MessageSender<RechargeUserCreditLog> implements InternalPaymentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RechargeUserCreditLogRepository rechargeUserCreditLogRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MessageChannel rechargeUserCreditChannel;

    @Override
    public User userCreditUpdateCredit(User user, BigDecimal credit, RechargeUserCreditType type) {
        Optional<User> userCredit = userRepository.findById(user.getId());
        User userInstance;

        // LOG Transaction
        RechargeUserCreditLog log = new RechargeUserCreditLog();
        log.setNewCredit(credit);
        log.setRechargeDateTime(LocalDateTime.now());
        log.setRechargeUserCreditType(type);

        userInstance = userCredit.get();
        log.setUser(userInstance);
        log.setOldCredit(userInstance.getCredit());
        userInstance.setCredit(credit);
        userInstance = userRepository.save(userInstance);

        log.setUser(userInstance);

        if (log.getOldCredit().compareTo(log.getNewCredit()) != 0) {
            rechargeUserCreditLogRepository.save(log);
            sendMessage(rechargeUserCreditChannel, log);
        }

        return userInstance;
    }

    @Override
    public Order processUserOrder(Order order) throws GassmanAvailableQuantityException {
        if(order.getProduct().getAvailableQuantity()!=null) {
            Integer updateAvailableQuantity = order.getProduct().getAvailableQuantity() - order.getQuantity();
            if (updateAvailableQuantity < 0) {
                throw new GassmanAvailableQuantityException();
            } else {
                order.getProduct().setAvailableQuantity(updateAvailableQuantity);
                productRepository.save(order.getProduct());
            }
        }
        order.setAmount(processOrderPrice(order));
        return orderRepository.save(order);
    }

    @Override
    public void processUserCancellation(User user) {
        rechargeUserCreditLogRepository.deleteAllByUser(user);
    }

    @Override
    public void processOrderCancellation(Order msg) {
        Optional<Order> order = orderRepository.findById(msg.getOrderId());
        if (order.isPresent()) {
            Optional<Payment> payment = paymentRepository.findByOrderId(msg.getOrderId());
            if (payment.isPresent()) {
                BigDecimal actualCredit = order.get().getUser().getCredit();
                BigDecimal newCredit = actualCredit.add(order.get().getAmount());
                this.userCreditUpdateCredit(msg.getUser(), newCredit, RechargeUserCreditType.ORDER_CANCELLED);
                paymentRepository.deleteById(payment.get().getPaymentId());
            }

            if(order.get().getProduct().getAvailableQuantity()!=null) {
                Integer updateAvailableQuantity = order.get().getProduct().getAvailableQuantity() + order.get().getQuantity();
                order.get().getProduct().setAvailableQuantity(updateAvailableQuantity);
                productRepository.save(order.get().getProduct());
            }
            orderRepository.deleteById(msg.getOrderId());
        }
    }

    @Override
    public BigDecimal processOrderPrice(Order msg) {
        return msg.getQuantity()!=null ? msg.getProduct().getPrice().multiply(BigDecimal.valueOf(msg.getQuantity())) : msg.getProduct().getPrice();
    }
}

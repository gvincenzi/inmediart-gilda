package org.inmediart.api.controller;

import org.inmediart.commons.binding.GassmanMessage;
import org.inmediart.commons.binding.MessageSender;
import org.inmediart.model.entity.*;
import org.inmediart.model.repository.OrderRepository;
import org.inmediart.model.repository.PaymentRepository;
import org.inmediart.model.repository.RechargeUserCreditLogRepository;
import org.inmediart.model.repository.UserRepository;
import org.inmediart.model.service.InternalPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/internal-credit/")
public class InternalCreditController extends MessageSender<Payment> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RechargeUserCreditLogRepository rechargeUserCreditLogRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MessageChannel orderPaymentConfirmationChannel;

    @Autowired
    private InternalPaymentService internalPaymentService;

    @Value("${message.userNotFound}")
    public String userNotFound;

    @Value("${message.insufficientCredit}")
    public String insufficientCredit;

    @Value("${message.alreadyPaid}")
    public String alreadyPaid;

    @Value("${message.paymentApproved}")
    public String paymentApproved;

    @Value("${message.orderNotExist}")
    public String orderNotExist;

    @PutMapping("/{credit}")
    public ResponseEntity<User> addCredit(@RequestBody User user, @PathVariable("credit") BigDecimal credit) {
        Optional<User> userCreditCurrent = userRepository.findById(user.getId());
        if(userCreditCurrent.isPresent()){
            credit = credit.add(userCreditCurrent.get().getCredit());
            return new ResponseEntity<>(internalPaymentService.userCreditUpdateCredit(user,credit, RechargeUserCreditType.TELEGRAM), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(userNotFound,user.getId()), null);
        }
    }

    @GetMapping("/{userId}/log")
    public ResponseEntity<List<RechargeUserCreditLog>> findRechargeUserCreditLogByUserId(@PathVariable("userId") Long userId) {
        Optional<User> userCredit = userRepository.findById(userId);
        List<RechargeUserCreditLog> logs = new ArrayList<>();
        if (userCredit.isPresent()) {
            logs = rechargeUserCreditLogRepository.findAllByUserOrderByRechargeDateTimeDesc(userCredit.get());
        }

        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findCreditByUser(@PathVariable("userId") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(userNotFound,userId), null);
        }
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<List<Order>> findOrdersByUser(@PathVariable("userId") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(orderRepository.findByUser(user.get()), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(userNotFound,userId), null);
        }
    }

    @GetMapping("/totalUserCredit")
    public ResponseEntity<BigDecimal> totalUserCredit() {
        BigDecimal total = BigDecimal.ZERO;
        for(User user : userRepository.findAll()){
            total = total.add(user.getCredit());
        }
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    // FIXME GVI Chi utilizza questo metodo ?
    @DeleteMapping("/{userId}/order/{orderId}")
    public ResponseEntity<Boolean> findOrdersByUser(@PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            orderRepository.deleteById(orderId);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/order/{orderId}/pay")
    public ResponseEntity<String> makePayment(@PathVariable("orderId") Long orderId) {
        Optional<Order> orderToPay = orderRepository.findById(orderId);
        Order order = null;
        if (!orderToPay.isPresent()) {
        } else {
            order = orderToPay.get();
        }

        User user = order.getUser();
        if (user.getCredit().compareTo(order.getAmount()) < 0) {
            return new ResponseEntity<>(String.format(insufficientCredit, order.getAmount(), user.getCredit()), HttpStatus.OK);
        } else {
            Optional<Payment> paymentPeristed = paymentRepository.findByOrderId(order.getOrderId());
            if(paymentPeristed.isPresent()){
                return new ResponseEntity<>(String.format(alreadyPaid,order.getOrderId()), HttpStatus.OK);
            } else {
                Payment payment = new Payment();
                payment.setPaymentId("INTERNAL_PAYID_" + System.currentTimeMillis());
                payment.setPaymentDateTime(LocalDateTime.now());
                payment.setOrderId(order.getOrderId());
                payment.setPaymentType(PaymentType.INTERNAL_CREDIT);
                payment.setAmount(order.getAmount());
                paymentRepository.save(payment);
                BigDecimal newCredit = user.getCredit().subtract(order.getAmount());
                user.setCredit(newCredit);
                userRepository.save(user);

                Optional<Order> orderPersisted = orderRepository.findById(payment.getOrderId());
                if (orderPersisted.isPresent()) {
                    orderPersisted.get().setPaid(Boolean.TRUE);
                    orderPersisted.get().setPaymentExternalReference(payment.getPaymentId());
                    orderPersisted.get().setPaymentExternalDateTime(payment.getPaymentDateTime());
                    orderPersisted.get().setAmount(payment.getAmount());
                    orderRepository.save(orderPersisted.get());

                    Message<GassmanMessage<Order>> msg = MessageBuilder.withPayload(new GassmanMessage<>(orderPersisted.get(),instanceId,botName)).build();
                    orderPaymentConfirmationChannel.send(msg);
                }

                return new ResponseEntity<>(paymentApproved, HttpStatus.OK);
            }
        }
    }
}

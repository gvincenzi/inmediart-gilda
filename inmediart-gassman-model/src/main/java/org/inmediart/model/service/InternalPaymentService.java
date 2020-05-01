package org.inmediart.model.service;

import org.inmediart.model.entity.Order;
import org.inmediart.model.entity.RechargeUserCreditType;
import org.inmediart.model.entity.User;

import java.math.BigDecimal;

public interface InternalPaymentService {
    User userCreditUpdateCredit(User user, BigDecimal credit, RechargeUserCreditType type);
    Order processUserOrder(Order msg);
    void processUserCancellation(User msg);
    void processOrderCancellation(Order msg);
    BigDecimal processOrderPrice(Order msg);
}

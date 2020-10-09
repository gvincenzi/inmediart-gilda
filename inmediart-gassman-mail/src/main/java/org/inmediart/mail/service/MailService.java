package org.inmediart.mail.service;

import org.inmediart.mail.dto.OrderDTO;
import org.inmediart.mail.dto.RechargeUserCreditLogDTO;
import org.inmediart.mail.dto.UserDTO;

public interface MailService {
    void sendRegistrationMessage(UserDTO userDTO);
    void sendUserCancellationMessage(UserDTO userDTO);
    void sendOrderMessage(OrderDTO orderDTO);
    void sendOrderUpdateMessage(OrderDTO orderDTO);
    void sendRechargeUserCreditMessage(RechargeUserCreditLogDTO rechargeUserCreditLogDTO);
    void sendOrderPaymentConfirmationMessage(OrderDTO msg);
    void sendOrderCancellationMessage(OrderDTO msg);
    void sendProductUpdateMessage(OrderDTO msg);
}

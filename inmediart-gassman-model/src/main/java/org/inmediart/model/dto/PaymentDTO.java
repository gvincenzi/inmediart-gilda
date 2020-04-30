package org.inmediart.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private String paymentId;
    private LocalDateTime paymentDateTime;
    private Long orderId;
    private BigDecimal amount;
}

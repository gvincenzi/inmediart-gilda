package org.inmediart.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeUserCreditLogDTO {
    private Long id;
    private UserDTO user;
    private String rechargeUserCreditType;
    private LocalDateTime rechargeDateTime;
    private BigDecimal oldCredit;
    private BigDecimal newCredit;
}

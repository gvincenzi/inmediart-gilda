package org.inmediart.gui.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.inmediart.model.entity.type.ActionType;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderDTO implements Comparable<OrderDTO>{
    private Long orderId;
    private ActionType actionType;
    private UserDTO user;
    private ProductDTO product;
    private Boolean paid = Boolean.FALSE;
    private BigDecimal amount;
    private String paymentExternalReference;
    private LocalDateTime paymentExternalDateTime;

    @Override
    public int compareTo(OrderDTO orderDTO) {
        return this.orderId.compareTo(orderDTO.orderId);
    }

    @Override
    public String toString() {
        return "\nID : " + orderId +
                "\nConteunto : " + product +
                "\nTipo di ordine : " + actionType.getLabel() +
                (paid ? "\n\n**Password : " + this.getProduct().getPassword() : "\n\n**Quest'ordine non è ancora stato pagato**");
    }
}

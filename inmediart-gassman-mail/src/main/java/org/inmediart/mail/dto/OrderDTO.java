package org.inmediart.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.inmediart.mail.dto.type.ActionType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderDTO implements Comparable<OrderDTO>{
    private Long orderId;
    private ActionType actionType;
    private UserDTO user;
    private ProductDTO product;
    private Boolean paid = Boolean.FALSE;
    private BigDecimal amount;
    private Integer quantity;

    @Override
    public int compareTo(OrderDTO orderDTO) {
        return this.orderId.compareTo(orderDTO.orderId);
    }

    @Override
    public String toString() {
        return "\nID : " + orderId +
                "\nContenuto : " + product +
                "\nTipo di ordine : " + actionType.getLabel() +
                (quantity!=null ? "\nQuantità : " + quantity : "" ) +
                (paid ? (this.getProduct().getPassword()!=null ? "\n\n**Password : " + this.getProduct().getPassword() : "") : "\n\n**Quest'ordine non è ancora stato pagato**");
    }
}

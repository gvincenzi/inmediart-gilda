package org.inmediart.gui.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.inmediart.model.entity.type.ActionType;

import java.math.BigDecimal;
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
    private Double quantity;
    private String paymentExternalReference;
    private LocalDateTime paymentExternalDateTime;
    private String address;

    @Override
    public int compareTo(OrderDTO orderDTO) {
        return this.orderId.compareTo(orderDTO.orderId);
    }

    @Override
    public String toString() {
        return "\nID : " + orderId +
                "\nConteunto : " + product +
                "\nTipo di ordine : " + actionType.getLabel() +
                (quantity!=null ? "\nQuantità : " + quantity : StringUtils.EMPTY ) +
                (address!=null ? "\nIndirizzo di spedizione : " + address : StringUtils.EMPTY ) +
                (paid ? (this.getProduct().getPassword()!=null ? "\n\n**Password : " + this.getProduct().getPassword() : StringUtils.EMPTY) : "\n\n**Quest'ordine non è ancora stato pagato**");
    }
}

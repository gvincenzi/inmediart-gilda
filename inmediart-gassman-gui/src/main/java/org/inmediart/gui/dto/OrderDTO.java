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
                (quantity!=null ? "\nQuantità : " + quantity : "" ) +
                (address!=null ? "\nIndirizzo di spedizione : " + address : "" ) +
                (paid ? (this.getProduct().getPassword()!=null ? "\n\n**Password : " + this.getProduct().getPassword() : "") : "\n\n**Quest'ordine non è ancora stato pagato**");
    }
}

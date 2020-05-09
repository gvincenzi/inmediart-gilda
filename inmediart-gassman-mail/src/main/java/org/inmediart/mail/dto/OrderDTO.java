package org.inmediart.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.inmediart.mail.dto.type.ActionType;

import java.math.BigDecimal;
import java.text.NumberFormat;

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
    private String address;

    @Override
    public int compareTo(OrderDTO orderDTO) {
        return this.orderId.compareTo(orderDTO.orderId);
    }

    @Override
    public String toString() {
        return "\nID : " + orderId +
                "\nTipo di ordine : " + actionType.getLabel() +
                (quantity!=null ? "\nQuantità : " + quantity : StringUtils.EMPTY ) +
                (address!=null ? "\nIndirizzo di spedizione : " + address : StringUtils.EMPTY ) +
                (amount!=null ? "\nImporto totale : " + NumberFormat.getCurrencyInstance().format(amount) : StringUtils.EMPTY ) +
                "\n\n**** Dettagli del prodotto ****\n" + product +
                (paid ? (StringUtils.isNotEmpty(this.getProduct().getUrl()) ? "\n\nURL : " + this.getProduct().getUrl() : StringUtils.EMPTY) : StringUtils.EMPTY) +
                (paid ? (StringUtils.isNotEmpty(this.getProduct().getPassword()) ? "\n\n**Password : " + this.getProduct().getPassword() : StringUtils.EMPTY) : "\n\n**Quest'ordine non è ancora stato pagato**");
    }
}

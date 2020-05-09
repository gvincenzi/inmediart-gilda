package org.inmediart.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;

@Data
@NoArgsConstructor
public class ProductDTO implements Comparable<ProductDTO> {
    private Long productId;
    private String name;
    private String description;
    private String url;
    private BigDecimal price;
    private Integer availableQuantity;
    private String password;
    private Boolean active = Boolean.TRUE;
    private Boolean delivery = Boolean.FALSE;

    @Override
    public String toString() {
        return  "Nome :'" + name + '\'' +
                "\nDescrizione :'" + description + '\''+
                "\nPrezzo :'" + NumberFormat.getCurrencyInstance().format(price) + '\'' +
                (delivery!=null && delivery ? " (con consegna a domicilio)" : StringUtils.EMPTY);
    }

    @Override
    public int compareTo(ProductDTO productDTO) {
        return this.name.compareTo(productDTO.name);
    }
}

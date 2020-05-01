package org.inmediart.telegram.bot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
    private String password;
    private Boolean active = Boolean.TRUE;

    @Override
    public String toString() {
        return  " Nome :'" + name + '\'' +
                "\nDescrizione :'" + description + '\''+
                "\nPrezzo :'" + NumberFormat.getCurrencyInstance().format(price) + '\'';
    }

    @Override
    public int compareTo(ProductDTO productDTO) {
        return this.name.compareTo(productDTO.name);
    }
}

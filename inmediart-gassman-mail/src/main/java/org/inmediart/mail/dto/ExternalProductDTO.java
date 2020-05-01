package org.inmediart.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;

@Data
@NoArgsConstructor
public class ExternalProductDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private String ownerBotName;

    @Override
    public String toString() {
        return  " BOT :'" + ownerBotName + '\'' +
                " Nome :'" + name + '\'' +
                "\nDescrizione :'" + description + '\''+
                "\nPrezzo :'" + NumberFormat.getCurrencyInstance().format(price) + '\'';
    }
}

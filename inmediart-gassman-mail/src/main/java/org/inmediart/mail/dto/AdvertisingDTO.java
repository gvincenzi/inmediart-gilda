package org.inmediart.mail.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdvertisingDTO {
    List<OrderDTO> orders;
    List<ProductDTO> productList;
    List<ExternalProductDTO> externalProducts;
    List<UserDTO> users;

    @JsonIgnore
    public String ordersToString() {
        if (orders.isEmpty()) {
            return "Non ci sono ordini in sospeso";
        } else {
            StringBuilder builder = new StringBuilder();
            for (OrderDTO orderDTO : orders) {
                builder.append(orderDTO.toString());
                builder.append("\n");
            }

            return builder.toString();
        }
    }

    @JsonIgnore
    public String productListToString() {
        if (productList.isEmpty()) {
            return "Non ci sono nuovi prodotti in catalogo";
        } else {
            StringBuilder builder = new StringBuilder();
            for (ProductDTO productDTO : productList) {
                builder.append(productDTO.toString());
                builder.append("\n");
            }

            return builder.toString();
        }
    }

    @JsonIgnore
    public String externalProductsToString() {
        if (externalProducts.isEmpty()) {
            return "Non ci sono nuovi prodotti nei cataloghi";
        } else {
            StringBuilder builder = new StringBuilder();
            for (ExternalProductDTO externalProductDTO : externalProducts) {
                builder.append(externalProductDTO.toString());
                builder.append("\n");
            }

            return builder.toString();
        }
    }
}

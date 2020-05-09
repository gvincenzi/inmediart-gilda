package org.inmediart.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inmediart_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String url;
    @Column
    private String password;
    @Column
    private BigDecimal price;
    @Column
    private Integer availableQuantity;
    @Column
    private Boolean active = Boolean.TRUE;
    @Column
    private Boolean advertising = Boolean.FALSE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId) &&
                name.equals(product.name) &&
                description.equals(product.description) &&
                url.equals(product.url) &&
                ((price==null && product.price==null) || price.equals(product.price)) &&
                ((availableQuantity==null && product.availableQuantity==null) || availableQuantity.equals(product.availableQuantity)) &&
                active.equals(product.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, description, active);
    }
}

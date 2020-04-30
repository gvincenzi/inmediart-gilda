package org.inmediart.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inmediart_external_product")
public class ExternalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long externalProductId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private BigDecimal price;
    @Column
    private String ownerBotName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalProduct product = (ExternalProduct) o;
        return externalProductId.equals(product.externalProductId) &&
                name.equals(product.name) &&
                ownerBotName.equals(product.ownerBotName) &&
                description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalProductId, name, description, ownerBotName);
    }
}

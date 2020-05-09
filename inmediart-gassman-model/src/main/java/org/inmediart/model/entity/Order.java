package org.inmediart.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.inmediart.model.entity.type.ActionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inmediart_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    @Column
    private ActionType actionType;
    @Column(nullable = false)
    private Boolean paid = Boolean.FALSE;
    @Column
    private String paymentExternalReference;
    @Column
    private LocalDateTime paymentExternalDateTime;
    @Column
    private BigDecimal amount;
    @Column
    private Integer quantity;
    @Column
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="productId", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id", nullable = false)
    private User user;
}

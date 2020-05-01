package org.inmediart.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inmediart_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(nullable = false)
    private String mail;
    @Column
    private Integer telegramUserId;
    @Column
    private Boolean active = Boolean.TRUE;
    @Column
    private Boolean administrator = Boolean.FALSE;
    @Column
    private BigDecimal credit = BigDecimal.ZERO;
}

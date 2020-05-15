package org.inmediart.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.inmediart.model.entity.type.ActionType;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "inmediart_action")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String userMailFilter;
    @Column
    private Integer telegramUserIdToManage;
    @Column
    private Long productIdToManage;
    @Column
    private ActionType actionType;
    @Column
    private Integer telegramUserId;
    @Column
    private Long selectedProductId;
    @Column
    private Double quantity;
    @Column
    private Boolean inProgress = Boolean.TRUE;
}

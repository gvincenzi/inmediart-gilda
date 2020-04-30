package org.inmediart.model.repository;

import org.inmediart.model.entity.Order;
import org.inmediart.model.entity.Product;
import org.inmediart.model.entity.User;
import org.inmediart.model.entity.type.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByProduct(Product product);
    List<Order> findByUser(User user);
    List<Order> findByActionType(ActionType actionType);
    List<Order> findByPaidFalse();
}

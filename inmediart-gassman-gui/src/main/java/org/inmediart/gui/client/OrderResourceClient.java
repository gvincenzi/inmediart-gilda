package org.inmediart.gui.client;

import org.inmediart.gui.configuration.FeignClientConfiguration;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.model.entity.type.ActionType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "orders", url = "${feign.url}/orders", configuration = FeignClientConfiguration.class)
public interface OrderResourceClient {
    @GetMapping("/users/{id}")
    List<OrderDTO> findAllOrdersByUser(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    OrderDTO findOrderById(@PathVariable("id") Long id);

    @PostMapping("/telegram")
    OrderDTO postOrder(@RequestBody OrderDTO order);

    @DeleteMapping("/{id}")
    void deleteOrder(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    OrderDTO updateOrder(@PathVariable("id") Long id, @RequestBody OrderDTO orderDTO);

    @GetMapping("/action/{actionType}")
    List<OrderDTO> findOrdersByActionType(@PathVariable("actionType") ActionType actionType);

    @GetMapping("/action/{actionType}/all")
    List<OrderDTO> findAllOrdersByActionType(@PathVariable("actionType") ActionType actionType);
}

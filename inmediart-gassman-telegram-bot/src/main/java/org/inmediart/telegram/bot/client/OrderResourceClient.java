package org.inmediart.telegram.bot.client;

import org.inmediart.telegram.bot.configuration.FeignClientConfiguration;
import org.inmediart.telegram.bot.dto.OrderDTO;
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
}

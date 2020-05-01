package org.inmediart.telegram.bot.client;

import org.inmediart.telegram.bot.configuration.FeignClientConfiguration;
import org.inmediart.telegram.bot.dto.OrderDTO;
import org.inmediart.telegram.bot.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products", url = "${feign.url}/products", configuration = FeignClientConfiguration.class)
public interface ProductResourceClient {
    @GetMapping()
    List<ProductDTO> findAll();

    @GetMapping("/{id}/orders")
    List<OrderDTO> findProductOrders(@PathVariable Long id);

    @PostMapping()
    ProductDTO postProduct(@RequestBody ProductDTO product);

    @GetMapping("/{id}")
    ProductDTO findProductById(@PathVariable Long id);
}

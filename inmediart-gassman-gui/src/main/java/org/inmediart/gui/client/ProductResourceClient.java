package org.inmediart.gui.client;

import org.inmediart.gui.configuration.FeignClientConfiguration;
import org.inmediart.gui.dto.OrderDTO;
import org.inmediart.gui.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "products", url = "${feign.url}/products", configuration = FeignClientConfiguration.class)
public interface ProductResourceClient {
    @GetMapping
    List<ProductDTO> findActives();

    @GetMapping("/all")
    List<ProductDTO> findAll();

    @GetMapping("/{id}/orders")
    List<OrderDTO> findProductOrders(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    ProductDTO findById(@PathVariable("id") Long id);

    @PostMapping()
    ProductDTO postProduct(@RequestBody ProductDTO product);

    @GetMapping("/{id}")
    ProductDTO findProductById(@PathVariable("id") Long id);

    @PutMapping("/{id}")
    ProductDTO updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO);

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") Long id);
}

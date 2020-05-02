package org.inmediart.api.controller;

import org.inmediart.commons.messaging.MessageSender;
import org.inmediart.model.entity.ExternalProduct;
import org.inmediart.model.entity.Order;
import org.inmediart.model.entity.Product;
import org.inmediart.model.repository.ExternalProductRepository;
import org.inmediart.model.repository.OrderRepository;
import org.inmediart.model.repository.ProductRepository;
import org.inmediart.model.service.ExternalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController extends MessageSender<Order> {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ExternalProductRepository externalProductRepository;

    @Autowired
    private ExternalProductService externalProductService;

    @Autowired
    private MessageChannel orderCancellationChannel;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(productRepository.findByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> findProductOrders(@PathVariable("id") Long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            return new ResponseEntity<>(orderRepository.findByProduct(product.get()), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> findProductById(@PathVariable("id") Long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @PostMapping
    public ResponseEntity<Product> postProduct(@RequestBody Product product){
        Product productPersisted = productRepository.save(product);
        externalProductService.sendProductToOtherGassman(productPersisted);
        return new ResponseEntity<>(productPersisted, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable("id") Long id, @RequestBody Product productToUpdate){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            productToUpdate.setProductId(id);
            if(!product.get().equals(productToUpdate)){
                productToUpdate = productRepository.save(productToUpdate);
            }
            return new ResponseEntity<>(productToUpdate, HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long id){
        if(productRepository.existsById(id)){
            Optional<Product> productToDelete = productRepository.findById(id);
            List<Order> orders = orderRepository.findByProduct(productToDelete.get());
            orderRepository.deleteAll(orders);
            sendUserOrdersCancellationMessage(orders);

            productRepository.deleteById(id);

            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @GetMapping("/external")
    public ResponseEntity<List<ExternalProduct>> findExternalProduct(){
        return new ResponseEntity<>(externalProductRepository.findAll(), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/external/{id}")
    public ResponseEntity<String> deleteExternalProductById(@PathVariable("id") Long id){
        externalProductRepository.deleteById(id);
        return new ResponseEntity<>("External product deleted", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/external/all")
    public ResponseEntity<String> deleteExternalProduct(){
        long count = externalProductRepository.count();
        externalProductRepository.deleteAll();
        return new ResponseEntity<>(String.format("Deleted %d external products",count), HttpStatus.OK);
    }

    private void sendUserOrdersCancellationMessage(List<Order> orders) {
        for(Order order : orders) {
            sendMessage(orderCancellationChannel,order);
        }
    }
}

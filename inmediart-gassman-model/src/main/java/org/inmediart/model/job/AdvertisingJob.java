package org.inmediart.model.job;

import org.inmediart.commons.messaging.MessageSenderThread;
import org.inmediart.model.dto.AdvertisingDTO;
import org.inmediart.model.entity.ExternalProduct;
import org.inmediart.model.entity.Order;
import org.inmediart.model.entity.Product;
import org.inmediart.model.repository.ExternalProductRepository;
import org.inmediart.model.repository.OrderRepository;
import org.inmediart.model.repository.ProductRepository;
import org.inmediart.model.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class AdvertisingJob extends MessageSenderThread<AdvertisingDTO> {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExternalProductRepository externalProductRepository;

    @Autowired
    private MessageChannel advertisingChannel;

    @Override
    public void run() {
        List<Order> orders = orderRepository.findByPaidFalse();
        List<Product> productList = productRepository.findByActiveTrueAndAdvertisingFalse();
        List<ExternalProduct> externalProducts = externalProductRepository.findAll();

        if (!productList.isEmpty() || !externalProducts.isEmpty() || !orders.isEmpty()) {
            AdvertisingDTO advertisingDTO = new AdvertisingDTO();
            advertisingDTO.setExternalProducts(externalProducts);
            advertisingDTO.setOrders(orders);
            advertisingDTO.setProductList(productList);
            advertisingDTO.setUsers(userRepository.findByActiveTrue());
            sendMessage(advertisingChannel, advertisingDTO);
            for (Product product : productList) {
                product.setAdvertising(Boolean.TRUE);
                productRepository.save(product);
            }
            for (ExternalProduct product : externalProducts) {
                externalProductRepository.delete(product);
            }
        }
    }
}

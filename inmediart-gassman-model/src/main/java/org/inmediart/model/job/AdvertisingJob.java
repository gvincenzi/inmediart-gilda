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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AdvertisingJob extends MessageSenderThread<AdvertisingDTO> implements InitializingBean {
    @Value("${job.delay}")
    public long delay;

    @Value("${job.hour}")
    public int hour;

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

    private static AdvertisingJob instance;

    @Override
    public void afterPropertiesSet() {
        instance = this;
    }

    public static AdvertisingJob getInstance() {
        return instance;
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep(delay);
            } catch (InterruptedException e) {
            }
            LocalDateTime now = LocalDateTime.now();
            if(now.getHour() == hour && DayOfWeek.SATURDAY.equals(now.getDayOfWeek())) {
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
    }
}

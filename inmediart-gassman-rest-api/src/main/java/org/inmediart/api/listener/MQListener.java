package org.inmediart.api.listener;

import org.inmediart.api.binding.RestAPIMQBinding;
import org.inmediart.commons.messaging.GassmanMessage;
import org.inmediart.commons.messaging.MessageSender;
import org.inmediart.model.entity.ExternalProduct;
import org.inmediart.model.entity.Order;
import org.inmediart.model.repository.ExternalProductRepository;
import org.inmediart.model.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(RestAPIMQBinding.class)
public class MQListener extends MessageSender<Order> {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ExternalProductRepository externalProductRepository;

    @Value("${gassman.instance.id}")
    private String instanceId;

    @Value("${gassman.instance.botName}")
    private String botName;

    @StreamListener(target = RestAPIMQBinding.EXTERNAL_PRODUCT_INPUT)
    public void processExternalProduct(GassmanMessage<ExternalProduct> msg) {
        if (!checkInstance(msg)) {
            externalProductRepository.save(msg.getPayload());
        }
    }

    private boolean checkInstance(GassmanMessage msg) {
        return msg.getParams() != null && msg.getParams().length>=2
                && instanceId.equalsIgnoreCase(msg.getParams()[0])
                && botName.equalsIgnoreCase(msg.getParams()[1]);
    }
}

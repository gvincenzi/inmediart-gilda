package org.inmediart.model.service;

import org.inmediart.commons.messaging.MessageSender;
import org.inmediart.model.entity.ExternalProduct;
import org.inmediart.model.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class ExternalProductServiceImpl extends MessageSender<ExternalProduct> implements ExternalProductService {
    @Autowired
    private MessageChannel externalProductChannel;

    @Override
    public void sendProductToOtherGassman(Product product) {
        ExternalProduct externalProduct = new ExternalProduct();
        externalProduct.setPrice(product.getPrice());
        externalProduct.setName(product.getName());
        externalProduct.setDescription(product.getDescription());
        externalProduct.setOwnerBotName(botName);
        sendMessage(externalProductChannel,externalProduct);
    }
}

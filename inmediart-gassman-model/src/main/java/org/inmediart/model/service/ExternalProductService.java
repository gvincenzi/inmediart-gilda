package org.inmediart.model.service;

import org.inmediart.model.entity.Product;

public interface ExternalProductService {
    void sendProductToOtherGassman(Product product);
}

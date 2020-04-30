package org.inmediart.model.dto;

import lombok.Data;
import org.inmediart.model.entity.ExternalProduct;
import org.inmediart.model.entity.Order;
import org.inmediart.model.entity.Product;
import org.inmediart.model.entity.User;

import java.util.List;

@Data
public class AdvertisingDTO {
    List<Order> orders;
    List<Product> productList;
    List<ExternalProduct> externalProducts;
    List<User> users;
}

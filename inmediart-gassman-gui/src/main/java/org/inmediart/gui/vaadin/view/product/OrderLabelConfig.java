package org.inmediart.gui.vaadin.view.product;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "label.order")
public class OrderLabelConfig {
    private String user;
    private String actionType;
    private String amount;
    private String paid;
    private String paymentExternalReference;
    private String paymentExternalDateTime;
    private String showDone;
}

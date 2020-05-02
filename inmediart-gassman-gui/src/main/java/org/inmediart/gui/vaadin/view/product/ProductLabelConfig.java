package org.inmediart.gui.vaadin.view.product;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "label.product")
public class ProductLabelConfig {
    private String name;
    private String description;
    private String url;
    private String password;
    private String price;
    private String active;
    private String showAll;
}

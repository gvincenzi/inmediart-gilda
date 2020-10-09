package org.inmediart.api;

import org.inmediart.api.binding.RestAPIMQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(RestAPIMQBinding.class)
@SpringBootApplication(scanBasePackages={"org.inmediart.commons","org.inmediart.model","org.inmediart.api"})
public class InmediartRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InmediartRestApiApplication.class, args);
    }

}

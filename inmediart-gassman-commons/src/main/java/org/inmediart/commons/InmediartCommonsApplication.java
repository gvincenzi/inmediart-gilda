package org.inmediart.commons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"org.inmediart.commons"})
public class InmediartCommonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InmediartCommonsApplication.class, args);
    }

}

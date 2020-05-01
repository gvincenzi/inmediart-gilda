package org.inmediart.mail;

import org.inmediart.mail.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(MQBinding.class)
@SpringBootApplication
public class InmediartMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(InmediartMailApplication.class, args);
    }

}

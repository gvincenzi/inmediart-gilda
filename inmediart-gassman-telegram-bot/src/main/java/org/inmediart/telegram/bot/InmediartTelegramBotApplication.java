package org.inmediart.telegram.bot;

import org.inmediart.commons.binding.MQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableBinding(MQBinding.class)
@EnableFeignClients
@SpringBootApplication
public class InmediartTelegramBotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(InmediartTelegramBotApplication.class, args);
    }

}

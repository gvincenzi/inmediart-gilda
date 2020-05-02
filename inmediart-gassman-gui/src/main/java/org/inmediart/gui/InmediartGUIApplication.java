package org.inmediart.gui;

import org.inmediart.gui.binding.GUIMQBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableBinding(GUIMQBinding.class)
@EnableFeignClients
@SpringBootApplication(scanBasePackages={"org.inmediart.commons","org.inmediart.model","org.inmediart.gui"})
public class InmediartGUIApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(InmediartGUIApplication.class, args);
    }

}

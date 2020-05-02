package org.inmediart.model;

import org.inmediart.model.job.JobConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"org.inmediart.commons","org.inmediart.model"})
public class InmediartModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(InmediartModelApplication.class, args);
        JobConfiguration.startJobs();
    }

}

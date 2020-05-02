package org.inmediart.model.job;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {
    public static void startJobs(){
        AdvertisingJob.getInstance().start();
    }
}

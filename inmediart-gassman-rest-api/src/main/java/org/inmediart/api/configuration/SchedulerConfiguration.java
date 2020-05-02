package org.inmediart.api.configuration;

import org.inmediart.model.job.AdvertisingJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SchedulerConfiguration {
    @Autowired
    private ApplicationContext context;

    private AdvertisingJob advertisingJob;

    @Scheduled(cron = "${job.cron}")
    public void executeTask1() {
        advertisingJob = (AdvertisingJob) context.getBean("advertisingJob");
        advertisingJob.start();
    }
}

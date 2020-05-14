package org.inmediart.mail.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailTemplateConfiguration {

    @Value("${template.registration}")
    public String templateRegistration;

    @Value("${template.userCancellationMessage}")
    public String templateUserCancellationMessage;

    @Value("${template.creditrechargeConfirmation}")
    public String templateCreditRechargeConfirmation;

    @Value("${template.order}")
    public String templateOrder;

    @Value("${template.orderUpdate}")
    public String templateOrderUpdate;

    @Value("${template.paymentConfirmation}")
    public String templatePaymentConfirmation;

    @Value("${template.orderCancellationMessage}")
    public String templateOrderCancellationMessage;

    @Value("${template.advertisingMessage}")
    public String templateAdvertisingMessage;

    @Value("${template.productUpdateMessage}")
    public String templateProductUpdateMessage;

    @Value("${mail.username}")
    public String mailUsername;

    @Value("${mail.password}")
    public String mailPassword;

    @Value("${mail.host}")
    public String mailHost;

    @Value("${mail.port}")
    public Integer mailPort;

    @Bean
    public SimpleMailMessage templateRegistrationMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateRegistration);
        return message;
    }

    @Bean
    public SimpleMailMessage templateUserCancellationMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateUserCancellationMessage);
        return message;
    }

    @Bean
    public SimpleMailMessage templateCreditRechargeConfirmationMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateCreditRechargeConfirmation);
        return message;
    }

    @Bean
    public SimpleMailMessage templateOrderMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateOrder);
        return message;
    }

    @Bean
    public SimpleMailMessage templateOrderUpdateMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateOrderUpdate);
        return message;
    }

    @Bean
    public SimpleMailMessage templatePaymentConfirmationMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templatePaymentConfirmation);
        return message;
    }

    @Bean
    public SimpleMailMessage templateOrderCancellationMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateOrderCancellationMessage);
        return message;
    }

    @Bean
    public SimpleMailMessage templateAdvertisingMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateAdvertisingMessage);
        return message;
    }

    @Bean
    public SimpleMailMessage templateProductUpdateMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(templateProductUpdateMessage);
        return message;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}

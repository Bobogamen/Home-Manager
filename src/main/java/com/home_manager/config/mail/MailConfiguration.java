package com.home_manager.config.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

@Configuration
public class MailConfiguration {

    private final MailProperties mailProperties;

    public MailConfiguration(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setJavaMailProperties(setProperty(mailProperties.getProperties()));

        System.out.println(mailProperties.getUsername());
        System.out.println(mailProperties.getPassword());

        return  mailSender;
    }

    public Properties setProperty(Map<String, String> mailProperties) {
        Properties properties = new Properties();

        properties.putAll(mailProperties);

        return properties;
    }
}

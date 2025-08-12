package com.lubricentro.mantenimiento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String host;

    @Value("${spring.mail.port:587}")      // <- default evita parsear null
    private int port;

    @Value("${spring.mail.username:}")     // vacíos si no hay credenciales
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private boolean starttls;

    @Value("${spring.mail.properties.mail.smtp.ssl.trust:}") // si no viene, usamos host
    private String sslTrust;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);

        Properties p = sender.getJavaMailProperties();
        p.put("mail.transport.protocol", "smtp");
        p.put("mail.smtp.auth", String.valueOf(smtpAuth));
        p.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        p.put("mail.smtp.ssl.trust", (sslTrust == null || sslTrust.isBlank()) ? host : sslTrust);
        // p.put("mail.debug", "true"); // útil para diagnosticar

        return sender;
    }
}

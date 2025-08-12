package com.lubricentro.mantenimiento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    // Spring resuelve estas @Value desde variables de entorno (SPRING_MAIL_*)
    // o desde application.yml. Si no existen, usa el default del ":".
    @Value("${spring.mail.host:smtp.gmail.com}")
    private String host;

    @Value("${spring.mail.port:587}")
    private int port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private boolean smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
    private boolean starttls;

    @Value("${spring.mail.properties.mail.smtp.ssl.trust:}")
    private String sslTrust;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl ms = new JavaMailSenderImpl();
        ms.setHost(host);
        ms.setPort(port);
        ms.setUsername(username);
        ms.setPassword(password);

        Properties props = ms.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(smtpAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        props.put("mail.smtp.ssl.trust", (sslTrust == null || sslTrust.isBlank()) ? host : sslTrust);
        // props.put("mail.debug", "true"); // si querés ver logs detallados

        return ms;
    }
}

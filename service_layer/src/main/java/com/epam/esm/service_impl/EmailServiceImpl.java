package com.epam.esm.service_impl;

import com.epam.esm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    @Override
    public void sendSignupConfirmationEmail(String receiver, String confirmLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("Confirm registration");
        message.setText(String.format("To confirm your registration, please go to this link: %s", confirmLink));
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String receiver, String newPassword, String forwardLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("Password reset");
        message.setText(String.format(
                "Your new password will be: %s \nTo reset your password, please click go to this link : %s",
                newPassword, forwardLink));
        mailSender.send(message);
    }
}

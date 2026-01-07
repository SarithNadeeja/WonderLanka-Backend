package com.wanderlanka.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String receiver, String token){
        String verificationLink =  "http://localhost:3000/verify-email?token=" + token + "&email=" + receiver;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("infersio.ai@gmail.com");
        message.setTo(receiver);
        message.setSubject("Verify your Wonder Lanka account");
        message.setText("Click the link to verify your email:\n\n" + verificationLink);

        mailSender.send(message);
    }




}

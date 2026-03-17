package io.gianmarco.cleanArchitecture.infrastructure.services;

import io.gianmarco.cleanArchitecture.application.services.EmailSender;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.EmailDeliveryFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SmtpEmailSender implements EmailSender {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public SmtpEmailSender(
        JavaMailSender mailSender,
        @Value("${spring.mail.username}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                message,
                true,
                "UTF-8"
            );
            helper.setFrom(fromAddress); // ← esto faltaba
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailDeliveryFailedException();
        }
    }
}

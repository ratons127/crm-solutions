package com.betopia.hrm.webapp.util;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationSender {

    private final JavaMailSender mailSender;

    public NotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Common SMS method for leave
     */
    public SmsResponse sendSms(String mobileNumber, String messageBody) {
        System.out.println("Sending SMS to " + mobileNumber + ": " + messageBody);
        return new SmsResponse(null, mobileNumber, messageBody);
    }

    /**
     * Common Email method
     */
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML
            helper.setFrom("infohrm@betopiagroup.com");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}

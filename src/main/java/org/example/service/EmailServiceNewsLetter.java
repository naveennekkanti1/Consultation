package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.repository.NewsLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceNewsLetter {
    @Autowired
    public JavaMailSender mailSender;
    @Autowired
    public NewsLetterRepository newsletterRepository;

    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("srmcorporationservices@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendWelcomeEmail(String to) {
        String subject = "Welcome to SolAi Weekly Newsletter";
        String body = buildWelcomeEmailTemplate(to);
        sendHtmlEmail(to, subject, body);
    }

    public void sendWeeklyEmail(String to, String content) {
        String subject = "Weekly Newsletter from SolAi";
        String body = buildweeklyNewsTemplate(content);
        sendHtmlEmail(to, subject, body);
    }

    private String buildWelcomeEmailTemplate(String to) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #1a1f2e; color: white; padding: 30px; text-align: center; }
                    .content { padding: 30px; background: #f9f9f9; }
                    .footer { background: #1a1f2e; color: white; padding: 20px; text-align: center; font-size: 12px; }
                    .button { background: red; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block; margin: 20px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to SolAi!</h1>
                    </div>
                    <div class="content">
                        <h2>Thank you for subscribing!</h2>
                        <p>We're excited to have you join our community of AI enthusiasts and business innovators.</p>
                        <p>You'll receive weekly updates featuring:</p>
                        <ul>
                            <li>Latest AI technology trends</li>
                            <li>Business solution insights</li>
                            <li>Exclusive tips and strategies</li>
                            <li>Industry news and updates</li>
                        </ul>
                        <a href="https://localhost:3000" class="button">Visit Our Website</a>
                    </div>
                    <div class="footer">
                        <p>© 2023 SolAi. All rights reserved.</p>
                        <p>24th Main road, HSR Layout, Bengaluru, Karnataka, 560012</p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }

    private String buildweeklyNewsTemplate(String content) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #1a1f2e; color: white; padding: 30px; text-align: center; }
                    .content { padding: 30px; background: white; }
                    .footer { background: #1a1f2e; color: white; padding: 20px; text-align: center; font-size: 12px; }
                    .unsubscribe { color: #999; font-size: 11px; margin-top: 10px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>SolAi Weekly Newsletter</h1>
                    </div>
                    <div class="content">
                        %s
                    </div>
                    <div class="footer">
                        <p>© 2023 SolAi. All rights reserved.</p>
                        <p>24th Main Road,HSR Layout, Bengaluru, Karnataka, 560013</p>
                        <div class="unsubscribe">
                            <p>Don't want to receive these emails? <a href="aisol" style="color: #2563eb;">Unsubscribe</a></p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(content);
    }
}
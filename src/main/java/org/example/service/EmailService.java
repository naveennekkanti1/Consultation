package org.example.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("srmcorporationservices@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // <-- true = HTML enabled
        mailSender.send(message);
    }

    public void sendMeetLink(String to, String subject, String meetLink,
                             String meetingName, String meetingDate,
                             String meetingTime, String contactName) throws MessagingException {

        String htmlTemplate = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
                ".info-row { background: white; padding: 15px; margin: 10px 0; border-radius: 5px; border-left: 4px solid #667eea; }" +
                ".label { font-weight: bold; color: #667eea; }" +
                ".footer { text-align: center; margin-top: 20px; color: #666; font-size: 14px; }" +
                "a.button { display: inline-block; background: #667eea; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 15px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Consultation Meeting Scheduled</h1></div>" +
                "<div class='content'>" +
                "<p>Hi <strong>" + contactName + "</strong>,</p>" +
                "<p>Your consultation meeting has been scheduled successfully. Here are the details:</p>" +
                "<div class='info-row'><span class='label'>Meeting Name:</span> " + meetingName + "</div>" +
                "<div class='info-row'><span class='label'>Date:</span> " + meetingDate + "</div>" +
                "<div class='info-row'><span class='label'>Time:</span> " + meetingTime + "</div>" +
                "<p><a href='" + meetLink + "' class='button'>Join Google Meet</a></p>" +
                "<div class='footer'><p><strong>Regards,</strong><br>SolAi Team</p></div>" +
                "</div></div></body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("srmcorporationservices@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlTemplate, true);
        mailSender.send(message);
    }
}

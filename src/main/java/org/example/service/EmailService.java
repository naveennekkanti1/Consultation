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

    private static final String FROM_EMAIL =
            "srmcorporationservices@gmail.com";

    public void sendEmail(
            String to,
            String subject,
            String htmlContent
    ) throws MessagingException {

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        true,
                        "UTF-8"
                );

        helper.setFrom(FROM_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendFormInvitation(
            String to,
            String subject,
            String formTitle,
            String formLink
    ) throws MessagingException {

        String htmlContent =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<style>" +

                        "body {" +
                        "font-family: Arial, sans-serif;" +
                        "background-color: #f4f6f8;" +
                        "margin: 0;" +
                        "padding: 0;" +
                        "}" +

                        ".container {" +
                        "max-width: 600px;" +
                        "margin: 40px auto;" +
                        "background: white;" +
                        "border-radius: 12px;" +
                        "overflow: hidden;" +
                        "box-shadow: 0 4px 15px rgba(0,0,0,0.08);" +
                        "}" +

                        ".header {" +
                        "background: linear-gradient(135deg,#667eea,#764ba2);" +
                        "color: white;" +
                        "padding: 30px;" +
                        "text-align: center;" +
                        "}" +

                        ".content {" +
                        "padding: 30px;" +
                        "color: #333;" +
                        "line-height: 1.6;" +
                        "}" +

                        ".form-name {" +
                        "background: #f7f7ff;" +
                        "padding: 15px;" +
                        "border-left: 4px solid #667eea;" +
                        "margin: 20px 0;" +
                        "font-weight: bold;" +
                        "}" +

                        ".button-container {" +
                        "text-align: center;" +
                        "margin: 30px 0;" +
                        "}" +

                        ".button {" +
                        "display: inline-block;" +
                        "background: #667eea;" +
                        "color: white !important;" +
                        "padding: 14px 28px;" +
                        "text-decoration: none;" +
                        "border-radius: 7px;" +
                        "font-weight: bold;" +
                        "}" +

                        ".footer {" +
                        "text-align: center;" +
                        "color: #777;" +
                        "font-size: 13px;" +
                        "padding: 20px;" +
                        "}" +

                        "</style>" +
                        "</head>" +

                        "<body>" +

                        "<div class='container'>" +

                        "<div class='header'>" +
                        "<h1>Form Invitation</h1>" +
                        "</div>" +

                        "<div class='content'>" +

                        "<p>Hello,</p>" +

                        "<p>" +
                        "You have been invited to complete the following form:" +
                        "</p>" +

                        "<div class='form-name'>" +
                        formTitle +
                        "</div>" +

                        "<p>" +
                        "Please click the button below to open the form and submit your response." +
                        "</p>" +

                        "<div class='button-container'>" +

                        "<a href='" +
                        formLink +
                        "' class='button'>" +
                        "Open Form" +
                        "</a>" +

                        "</div>" +

                        "<p>" +
                        "If the button does not work, you can copy and paste the following link into your browser:" +
                        "</p>" +

                        "<p>" +
                        "<a href='" +
                        formLink +
                        "'>" +
                        formLink +
                        "</a>" +
                        "</p>" +

                        "</div>" +

                        "<div class='footer'>" +
                        "Regards,<br>" +
                        "<strong>SRM Corporation Services</strong>" +
                        "</div>" +

                        "</div>" +

                        "</body>" +
                        "</html>";

        sendEmail(
                to,
                subject,
                htmlContent
        );
    }

    public void sendMeetLink(
            String to,
            String subject,
            String meetLink,
            String meetingName,
            String meetingDate,
            String meetingTime,
            String contactName
    ) throws MessagingException {

        String htmlTemplate =
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<style>" +

                        "body {" +
                        "font-family: Arial, sans-serif;" +
                        "line-height: 1.6;" +
                        "color: #333;" +
                        "}" +

                        ".container {" +
                        "max-width: 600px;" +
                        "margin: 0 auto;" +
                        "padding: 20px;" +
                        "}" +

                        ".header {" +
                        "background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);" +
                        "color: white;" +
                        "padding: 30px;" +
                        "text-align: center;" +
                        "border-radius: 10px 10px 0 0;" +
                        "}" +

                        ".content {" +
                        "background: #f9f9f9;" +
                        "padding: 30px;" +
                        "border-radius: 0 0 10px 10px;" +
                        "}" +

                        ".info-row {" +
                        "background: white;" +
                        "padding: 15px;" +
                        "margin: 10px 0;" +
                        "border-radius: 5px;" +
                        "border-left: 4px solid #667eea;" +
                        "}" +

                        ".label {" +
                        "font-weight: bold;" +
                        "color: #667eea;" +
                        "}" +

                        ".footer {" +
                        "text-align: center;" +
                        "margin-top: 20px;" +
                        "color: #666;" +
                        "font-size: 14px;" +
                        "}" +

                        "a.button {" +
                        "display: inline-block;" +
                        "background: #667eea;" +
                        "color: white;" +
                        "padding: 10px 20px;" +
                        "text-decoration: none;" +
                        "border-radius: 5px;" +
                        "margin-top: 15px;" +
                        "}" +

                        "</style>" +
                        "</head>" +

                        "<body>" +

                        "<div class='container'>" +

                        "<div class='header'>" +
                        "<h1>Consultation Meeting Scheduled</h1>" +
                        "</div>" +

                        "<div class='content'>" +

                        "<p>Hi <strong>" +
                        contactName +
                        "</strong>,</p>" +

                        "<p>" +
                        "Your consultation meeting has been scheduled successfully. Here are the details:" +
                        "</p>" +

                        "<div class='info-row'>" +
                        "<span class='label'>Meeting Name:</span> " +
                        meetingName +
                        "</div>" +

                        "<div class='info-row'>" +
                        "<span class='label'>Date:</span> " +
                        meetingDate +
                        "</div>" +

                        "<div class='info-row'>" +
                        "<span class='label'>Time:</span> " +
                        meetingTime +
                        "</div>" +

                        "<p>" +
                        "<a href='" +
                        meetLink +
                        "' class='button'>" +
                        "Join Google Meet" +
                        "</a>" +
                        "</p>" +

                        "<div class='footer'>" +
                        "<p>" +
                        "<strong>Regards,</strong><br>" +
                        "SolAi Team" +
                        "</p>" +
                        "</div>" +

                        "</div>" +
                        "</div>" +

                        "</body>" +
                        "</html>";

        sendEmail(
                to,
                subject,
                htmlTemplate
        );
    }
}
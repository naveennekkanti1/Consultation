package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private GoogleMeetService googleMeetService;
    public void sendEmail(String to,String subject,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("srmcorporationservices@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
    public void sendMeetLink(String to, String subject, String meetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(
                "Hello,\n\n" +
                        "Your consultation meeting is scheduled.\n" +
                        "Google Meet link: " + meetLink + "\n\n" +
                        "Thanks!"
        );
        mailSender.send(message);
    }


}

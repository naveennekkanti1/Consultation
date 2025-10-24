package org.example.controller;

import jakarta.mail.MessagingException;
import org.example.constants.constant;
import org.example.entity.model;
import org.example.entity.MeetingModel;
import org.example.repository.MeetingRepository;
import org.example.repository.consultationRepository;
import org.example.service.EmailService;
import org.example.service.GoogleMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static org.example.constants.constant.SCHEDULED_FAILED;

@RestController
@RequestMapping("/consultation")
@CrossOrigin("*")
public class ConsultationController {
    @Autowired
    private consultationRepository repository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GoogleMeetService googleMeetService;

    @PostMapping("/request")
    public ResponseEntity<?> consultationRequest(@RequestBody model data) throws MessagingException {
        repository.save(data);
        String subject = "Consultation requested for " + data.getCompanyName();

        String message = "<!DOCTYPE html>" +
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
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Consultation Request Received</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Hi <strong>" + data.getContactPersonName() + "</strong>,</p>" +
                "<p>We have received your consultation request. Here are the details:</p>" +
                "<div class='info-row'><span class='label'>Company:</span> " + data.getCompanyName() + "</div>" +
                "<div class='info-row'><span class='label'>Email:</span> " + data.getContactPersonEmail() + "</div>" +
                "<div class='info-row'><span class='label'>Phone:</span> " + data.getContactPersonPhone() + "</div>" +
                "<div class='info-row'><span class='label'>Service Interested In:</span> " + data.getServiceInterestedIn() + "</div>" +
                "<div class='info-row'><span class='label'>Budget Range:</span> " + data.getBudgetRange() + "</div>" +
                "<div class='info-row'><span class='label'>Project Description:</span><br>" + data.getProjectDescription() + "</div>" +
                "<p style='margin-top: 30px;'>Our team will review your request and get back to you shortly.</p>" +
                "<div class='footer'>" +
                "<p><strong>Regards,</strong><br>SolAi Team</p>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(data.getContactPersonEmail(), subject, message);
        return ResponseEntity.status(constant.success).body("Message Sent Successfully");
    }

    @PostMapping("/meetingdetails")
    public ResponseEntity<?> meetingDetails(@RequestBody MeetingModel meetingModel) {
        try {
            String meetLink = googleMeetService.createGoogleMeet(meetingModel);
            System.out.println(meetLink);

            meetingModel.setMeetingLocation(meetLink);

            meetingRepository.save(meetingModel);

            String subject = "Consultation meeting requested for " + meetingModel.getMeetingName();
            String body = "Hello " + meetingModel.getContactName() + ",\n\n" +
                    "Your consultation meeting is scheduled at " + meetingModel.getMeetingDate() + " " +
                    meetingModel.getMeetingTime() + ".\n" +
                    "Google Meet link: " + meetLink + "\n\nThanks!";

            emailService.sendMeetLink(
                    meetingModel.getContactEmail(),
                    subject,
                    meetLink,
                    meetingModel.getMeetingName(),
                    meetingModel.getMeetingDate(),
                    meetingModel.getMeetingTime(),
                    meetingModel.getContactName()
            );

            return ResponseEntity.status(constant.meetscheduled).body("Message Sent Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(SCHEDULED_FAILED)
                    .body("Message Sent Failed: " + e.getMessage());
        }
    }

}

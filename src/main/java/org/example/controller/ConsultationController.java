package org.example.controller;

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
    public ResponseEntity<?> consultationRequest(@RequestBody model data){
        repository.save(data);
        String subject = "Consultation requested for "+data.getCompanyName();
        String message= "Hi "+data.getContactPersonName() +" We have Received Your Consultation Request"+
                        "\nEmail: "+data.getContactPersonEmail() +
                        "\nPhone Number: "+data.getContactPersonPhone() +
                        "\nService Interested In: "+data.getServiceInterestedIn()+
                        "\nBudget Range: "+data.getBudgetRange() +
                        "\nProject Description: "+data.getProjectDescription()+
                        "\n\n Regards\n SolAi";
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

            emailService.sendMeetLink(meetingModel.getContactEmail(), subject, body);

            return ResponseEntity.status(constant.meetscheduled).body("Message Sent Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(SCHEDULED_FAILED)
                    .body("Message Sent Failed: " + e.getMessage());
        }
    }

}

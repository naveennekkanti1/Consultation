package org.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "meetings")
public class MeetingModel {
    @Id
    private String id;
    private String meetingName;
    private String meetingDate;       // accepts "06-06-2026"
    private String meetingTime;       // accepts "3:00"
    private String meetingDescription;
    private String meetingLocation;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.mapping.Document;

@EntityScan
@AllArgsConstructor
@lombok.Data
@NoArgsConstructor
@Document(collection = "Meeting_details")
public class MeetingModel {
    private String meetingName;
    private String meetingDate;
    private String meetingTime;
    private String meetingDescription;
    private String meetingLocation;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}

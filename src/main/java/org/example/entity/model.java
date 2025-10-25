package org.example.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Document(collection = "CONSULTATION")
public class model {
    @Id
    private String id;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("contact_name")
    private String contactPersonName;
    @JsonProperty("contact_email")
    private String contactPersonEmail;
    @JsonProperty("contact_phone")
    private String contactPersonPhone;
    @JsonProperty("service")
    private String serviceInterestedIn;
    @JsonProperty("budget")
    private String budgetRange;
    @JsonProperty("description")
    private String projectDescription;
    @CreatedDate
    private ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
    @LastModifiedDate
    private Instant updatedAt;
    private String status = "received";

}

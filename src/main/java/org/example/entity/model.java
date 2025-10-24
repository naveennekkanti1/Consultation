package org.example.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.Instant;

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
    private Instant createdAt= Instant.now();

}

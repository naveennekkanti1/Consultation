package org.example.entity;
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
    private String companyName;
    private String contactPersonName;
    private String contactPersonEmail;
    private long contactPersonPhone;
    private String serviceInterestedIn;
    private String budgetRange;
    private String projectDescription;
    private Instant createdAt= Instant.now();

}

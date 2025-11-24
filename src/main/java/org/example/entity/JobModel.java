package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "jobs")
public class JobModel {
    @Id
    private String id;
    private String title;
    private String company;
    private String location;
    private String description;
    private String postedDate;
    private String lastDate;
    private String jobPostUrl;
    private int applyClicks = 0;
}

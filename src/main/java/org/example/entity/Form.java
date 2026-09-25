package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "forms")
public class Form {

    @Id
    private String id;

    private String title;

    private String description;

    private List<FormFields> fields;

    private FormSettings formsettings;

    // Users who are allowed to access this form
    private List<String> allowedEmails = new ArrayList<>();
}
package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "Forms")
public class Form {
    @Id
    private String id;
    private String title;
    private String description;
    private List<FormFields> fields;
    private FormSettings formsettings;
}

package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "form_responses")
public class FormResponse {
    @Id
    private String id;
    private String formId;
    private List<Map<String, String>> answers;
    private long submittedAt;
}

package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.FieldType;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FormFields {
    private String fieldId;
    private String label;
    private FieldType fieldType;
    private List<String> options;
    private boolean required;
}

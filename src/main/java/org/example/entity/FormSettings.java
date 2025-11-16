package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FormSettings {
    private boolean isAllowedMultipleResponses;
    private boolean isEditAfterSubmit;
    private String deadline;
    private boolean ispublic;
}

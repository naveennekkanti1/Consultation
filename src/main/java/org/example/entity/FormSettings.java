package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormSettings {

    // true  = users can submit multiple times
    // false = only one response per email
    private boolean allowedMultipleResponses;

    // Whether user can edit after submitting
    private boolean editAfterSubmit;

    // ISO date/time
    private String deadline;

    // true  = anyone can access
    // false = only users in allowedEmails
    private boolean publicForm;
}
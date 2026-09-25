package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormAccessRequest {

    private String email;

    // true = send email immediately
    private boolean sendEmail;
}
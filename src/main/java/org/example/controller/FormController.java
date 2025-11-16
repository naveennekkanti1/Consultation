package org.example.controller;

import org.example.dto.FormRequest;
import org.example.entity.Form;
import org.example.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/forms")
@CrossOrigin("*")
public class FormController {
    @Autowired
    private FormService formService;
    @PostMapping("/create")
    public ResponseEntity<?> createForm(@RequestBody FormRequest request)
    {
        Form createdForm = formService.createForm(request.getForm());
        String subject="Form created";
        formService.sendFormToRecipients(createdForm.getId(),request.getRecipients(),subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForm);
    }
}

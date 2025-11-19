package org.example.controller;

import org.example.dto.FormRequest;
import org.example.entity.Form;
import org.example.entity.FormResponse;
import org.example.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/forms")
@CrossOrigin("*")
public class FormController {
    @Autowired
    private FormService formService;

    @PostMapping("/create")
    public ResponseEntity<?> createForm(@RequestBody Form form) {
        if (form == null) {
            return ResponseEntity.badRequest().body("Form cannot be null");
        }

        Form saved = formService.createForm(form);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{formId}")
    public ResponseEntity<?> updateForm(
            @PathVariable String formId,
            @RequestBody Form updatedForm
    ) {
        Form form = formService.updateForm(formId, updatedForm);
        return ResponseEntity.ok(form);
    }

    // DELETE FORM
    @DeleteMapping("/{formId}")
    public ResponseEntity<?> deleteForm(@PathVariable String formId) {
        formService.deleteForm(formId);
        return ResponseEntity.ok("Form deleted successfully");
    }

    @PostMapping("/submit")
    public FormResponse submitForm(@RequestBody FormResponse response) {
        return formService.saveResponse(response);
    }

    @GetMapping("/get/{formId}")
    public List<FormResponse> getResponses(@PathVariable String formId) {
        return formService.getResponses(formId);
    }

    @GetMapping("/getAll")
    public List<Form> getAllForms() {
        return formService.getAllForms();
    }
    @GetMapping("/{formId}")
    public Form getFormById(@PathVariable String formId) {
        return formService.getForm(formId);
    }

}

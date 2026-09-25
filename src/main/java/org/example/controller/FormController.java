package org.example.controller;

import org.example.dto.FormAccessListRequest;
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


    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping("/create")
    public ResponseEntity<?> createForm(
            @RequestBody Form form
    ) {

        if (form == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Form cannot be null");
        }

        Form saved =
                formService.createForm(form);

        return ResponseEntity.ok(saved);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{formId}")
    public ResponseEntity<?> updateForm(
            @PathVariable String formId,
            @RequestBody Form updatedForm
    ) {

        Form form =
                formService.updateForm(
                        formId,
                        updatedForm
                );

        return ResponseEntity.ok(form);
    }


    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{formId}")
    public ResponseEntity<?> deleteForm(
            @PathVariable String formId
    ) {

        formService.deleteForm(formId);

        return ResponseEntity.ok(
                "Form deleted successfully"
        );
    }


    // ============================================================
    // SUBMIT
    // ============================================================

    @PostMapping("/submit")
    public ResponseEntity<?> submitForm(
            @RequestBody FormResponse response
    ) {

        try {

            FormResponse saved =
                    formService.saveResponse(
                            response
                    );

            return ResponseEntity.ok(saved);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // GET RESPONSES
    // ============================================================

    @GetMapping("/get/{formId}")
    public ResponseEntity<?> getResponses(
            @PathVariable String formId
    ) {

        return ResponseEntity.ok(
                formService.getResponses(formId)
        );
    }


    // ============================================================
    // GET ALL FORMS
    // ============================================================

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllForms() {

        return ResponseEntity.ok(
                formService.getAllForms()
        );
    }


    // ============================================================
    // GET FORM
    // ============================================================

    @GetMapping("/{formId}")
    public ResponseEntity<?> getFormById(
            @PathVariable String formId
    ) {

        return ResponseEntity.ok(
                formService.getForm(formId)
        );
    }


    // ============================================================
    // ADD ACCESS
    // ============================================================

    @PostMapping("/{formId}/access")
    public ResponseEntity<?> addAccess(
            @PathVariable String formId,
            @RequestBody FormAccessListRequest request
    ) {

        try {

            Form updatedForm =
                    formService.addAccess(
                            formId,
                            request
                    );

            return ResponseEntity.ok(
                    updatedForm
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // REMOVE ACCESS
    // ============================================================

    @DeleteMapping("/{formId}/access")
    public ResponseEntity<?> removeAccess(
            @PathVariable String formId,
            @RequestParam String email
    ) {

        try {

            Form updatedForm =
                    formService.removeAccess(
                            formId,
                            email
                    );

            return ResponseEntity.ok(
                    updatedForm
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ============================================================
    // GET ACCESS LIST
    // ============================================================

    @GetMapping("/{formId}/access")
    public ResponseEntity<?> getAllowedEmails(
            @PathVariable String formId
    ) {

        return ResponseEntity.ok(
                formService.getAllowedEmails(
                        formId
                )
        );
    }
}
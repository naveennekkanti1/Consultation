package org.example.service;

import org.example.constants.constant;
import org.example.dto.FormAccessListRequest;
import org.example.entity.Form;
import org.example.entity.FormResponse;
import org.example.entity.FormSettings;
import org.example.repository.FormRepository;
import org.example.repository.FormResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormResponseRepository responseRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    // ============================================================
    // CREATE FORM
    // ============================================================

    public Form createForm(Form form) {

        Assert.notNull(form, "Form must not be null");

        if (form.getAllowedEmails() == null) {
            form.setAllowedEmails(new ArrayList<>());
        }

        if (form.getFormsettings() == null) {
            form.setFormsettings(new FormSettings());
        }

        return formRepository.save(form);
    }


    // ============================================================
    // UPDATE FORM
    // ============================================================

    public Form updateForm(String formId, Form updatedForm) {

        Form existingForm = getForm(formId);

        existingForm.setTitle(updatedForm.getTitle());
        existingForm.setDescription(updatedForm.getDescription());
        existingForm.setFields(updatedForm.getFields());
        existingForm.setFormsettings(updatedForm.getFormsettings());

        // Do not accidentally delete access list
        // unless explicitly provided.
        if (updatedForm.getAllowedEmails() != null) {
            existingForm.setAllowedEmails(
                    normalizeEmails(updatedForm.getAllowedEmails())
            );
        }

        return formRepository.save(existingForm);
    }


    // ============================================================
    // DELETE FORM
    // ============================================================

    public void deleteForm(String formId) {

        Form form = getForm(formId);

        formRepository.delete(form);

        // Optional: delete all responses also
        List<FormResponse> responses =
                responseRepository.findByFormId(formId);

        responseRepository.deleteAll(responses);
    }


    // ============================================================
    // GET FORM
    // ============================================================

    public Form getForm(String formId) {

        return formRepository.findById(formId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Form not found with id: " + formId
                        ));
    }


    // ============================================================
    // GET ALL FORMS
    // ============================================================

    public List<Form> getAllForms() {
        return formRepository.findAll();
    }


    // ============================================================
    // ADD ACCESS TO FORM
    // ============================================================

    public Form addAccess(
            String formId,
            FormAccessListRequest request
    ) {

        Form form = getForm(formId);

        if (request == null ||
                request.getEmails() == null ||
                request.getEmails().isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one email is required"
            );
        }

        if (form.getAllowedEmails() == null) {
            form.setAllowedEmails(new ArrayList<>());
        }

        List<String> newEmails = normalizeEmails(request.getEmails());

        for (String email : newEmails) {

            if (!form.getAllowedEmails().contains(email)) {
                form.getAllowedEmails().add(email);
            }

            if (request.isSendEmail()) {
                sendFormEmail(
                        email,
                        form
                );
            }
        }

        return formRepository.save(form);
    }


    // ============================================================
    // REMOVE ACCESS
    // ============================================================

    public Form removeAccess(
            String formId,
            String email
    ) {

        Form form = getForm(formId);

        if (form.getAllowedEmails() == null) {
            return form;
        }

        String normalizedEmail =
                email.trim().toLowerCase();

        form.getAllowedEmails().remove(normalizedEmail);

        return formRepository.save(form);
    }


    // ============================================================
    // GET USERS WITH ACCESS
    // ============================================================

    public List<String> getAllowedEmails(String formId) {

        Form form = getForm(formId);

        if (form.getAllowedEmails() == null) {
            return new ArrayList<>();
        }

        return form.getAllowedEmails();
    }


    // ============================================================
    // SEND FORM TO EMAIL
    // ============================================================

    public void sendFormToRecipients(
            String formId,
            List<String> recipients,
            String subject
    ) {

        Form form = getForm(formId);

        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException(
                    "Recipients cannot be empty"
            );
        }

        for (String email : recipients) {

            String normalizedEmail =
                    email.trim().toLowerCase();

            // If private form, only send to users with access
            if (!isPublic(form) &&
                    !hasAccess(form, normalizedEmail)) {

                continue;
            }

            sendFormEmail(
                    normalizedEmail,
                    form,
                    subject
            );
        }
    }


    // ============================================================
    // SEND EMAIL
    // ============================================================

    private void sendFormEmail(
            String email,
            Form form
    ) {

        sendFormEmail(
                email,
                form,
                "You have been invited to complete a form"
        );
    }


    private void sendFormEmail(
            String email,
            Form form,
            String subject
    ) {

        String formLink =
                "http://your-frontend-url.com/forms/"
                        + form.getId();

        try {

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(email);
            message.setSubject(subject);

            message.setText(
                    "Hello,\n\n" +
                            "You have been invited to complete the following form:\n\n" +
                            form.getTitle() +
                            "\n\n" +
                            "Please click the link below:\n" +
                            formLink +
                            "\n\n" +
                            "Regards,\n" +
                            "Admin"
            );

            javaMailSender.send(message);

        } catch (Exception e) {

            throw new RuntimeException(
                    constant.EMAIL_SENT_FAILED +
                            ": " +
                            e.getMessage()
            );
        }
    }


    // ============================================================
    // SUBMIT FORM
    // ============================================================

    public FormResponse saveResponse(FormResponse response) {

        if (response == null) {
            throw new IllegalArgumentException(
                    "Response cannot be null"
            );
        }

        if (response.getFormId() == null ||
                response.getFormId().isBlank()) {

            throw new IllegalArgumentException(
                    "Form ID is required"
            );
        }

        // --------------------------------------------------------
        // GET FORM
        // --------------------------------------------------------

        Form form = formRepository
                .findById(response.getFormId())
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Form not found"
                        ));


        // --------------------------------------------------------
        // CHECK DEADLINE
        // --------------------------------------------------------

        validateDeadline(form);


        // --------------------------------------------------------
        // EMAIL REQUIRED
        // --------------------------------------------------------

        String email = response.getRespondentEmail();

        if (email == null || email.isBlank()) {

            throw new IllegalArgumentException(
                    "Respondent email is required"
            );
        }

        email = email.trim().toLowerCase();

        response.setRespondentEmail(email);


        // --------------------------------------------------------
        // PRIVATE FORM CHECK
        // --------------------------------------------------------

        if (!isPublic(form)) {

            if (!hasAccess(form, email)) {

                throw new RuntimeException(
                        "You do not have access to this form"
                );
            }
        }


        // --------------------------------------------------------
        // UNIQUE RESPONSE CHECK
        // --------------------------------------------------------

        if (!isMultipleResponsesAllowed(form)) {

            boolean alreadySubmitted =
                    responseRepository
                            .existsByFormIdAndRespondentEmail(
                                    response.getFormId(),
                                    email
                            );

            if (alreadySubmitted) {

                throw new RuntimeException(
                        "You have already submitted this form"
                );
            }
        }


        // --------------------------------------------------------
        // SAVE
        // --------------------------------------------------------

        response.setSubmittedAt(
                System.currentTimeMillis()
        );

        return responseRepository.save(response);
    }


    // ============================================================
    // GET RESPONSES
    // ============================================================

    public List<FormResponse> getResponses(String formId) {

        return responseRepository.findByFormId(formId);
    }


    // ============================================================
    // HELPER - PUBLIC FORM
    // ============================================================

    private boolean isPublic(Form form) {

        return form.getFormsettings() != null &&
                form.getFormsettings().isPublicForm();
    }


    // ============================================================
    // HELPER - MULTIPLE RESPONSES
    // ============================================================

    private boolean isMultipleResponsesAllowed(Form form) {

        return form.getFormsettings() != null &&
                form.getFormsettings()
                        .isAllowedMultipleResponses();
    }


    // ============================================================
    // HELPER - ACCESS
    // ============================================================

    private boolean hasAccess(
            Form form,
            String email
    ) {

        if (form.getAllowedEmails() == null) {
            return false;
        }

        String normalizedEmail =
                email.trim().toLowerCase();

        return form.getAllowedEmails()
                .stream()
                .filter(Objects::nonNull)
                .map(e -> e.trim().toLowerCase())
                .anyMatch(e ->
                        e.equals(normalizedEmail)
                );
    }


    // ============================================================
    // HELPER - DEADLINE
    // ============================================================

    private void validateDeadline(Form form) {

        if (form.getFormsettings() == null) {
            return;
        }

        String deadline =
                form.getFormsettings().getDeadline();

        if (deadline == null ||
                deadline.isBlank()) {

            return;
        }

        try {

            Instant deadlineInstant =
                    Instant.parse(deadline);

            if (Instant.now().isAfter(deadlineInstant)) {

                throw new RuntimeException(
                        "Form deadline expired"
                );
            }

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid deadline format"
            );
        }
    }


    // ============================================================
    // HELPER - NORMALIZE EMAILS
    // ============================================================

    private List<String> normalizeEmails(
            List<String> emails
    ) {

        return emails.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(e -> !e.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }
}
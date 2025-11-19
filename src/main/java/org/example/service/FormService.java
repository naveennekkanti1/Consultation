package org.example.service;

import org.example.entity.Form;
import org.example.entity.FormResponse;
import org.example.repository.FormRepository;
import org.example.constants.constant;
import org.example.repository.FormResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class FormService {
    @Autowired
    private FormRepository formRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private FormResponseRepository responseRepository;

    public Form createForm(Form form) {

        Assert.notNull(form, "Form must not be null");

        return formRepository.save(form);
    }


    public Form updateForm(String formId,Form updatedForm) {
        Form existingForm = formRepository.findById(formId)
                .orElseThrow(() -> new NoSuchElementException("Form not found with id: " + formId));

        // update fields
        existingForm.setTitle(updatedForm.getTitle());
        existingForm.setDescription(updatedForm.getDescription());
        existingForm.setFields(updatedForm.getFields());
        existingForm.setFormsettings(updatedForm.getFormsettings());

        return formRepository.save(existingForm);
    }

    public void deleteForm(String formId) {

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new NoSuchElementException("Form not found with id: " + formId));

        formRepository.delete(form);
    }
    public void sendFormToRecipients(String formId, List<String> recipients, String subject) {
        String formLink = "http://your-frontend-url.com/forms/" + formId;
        try {
            for (String email : recipients) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(subject);
                message.setText(
                        "Hello,\n\n" +
                                "A new form has been created. Click the link below to fill it:\n" +
                                formLink + "\n\n" +
                                "Regards,\nAdmin"
                );
                javaMailSender.send(message);
            }
        }catch (Exception e) {
            throw new EmailSendException(
                    constant.EMAIL_SENT_FAILED,e.getMessage()
            );
        }
    }


    public FormResponse saveResponse(FormResponse response) {

        // 1️⃣ Fetch form
        Form form = formRepository.findById(response.getFormId())
                .orElseThrow(() -> new RuntimeException("Form Not Found"));

        // 2️⃣ Validate deadline
        if (form.getFormsettings() != null && form.getFormsettings().getDeadline() != null) {
            try {
                // Parse ISO 8601 format or datetime-local format
                String deadlineStr = form.getFormsettings().getDeadline();
                Instant deadlineInstant;

                // Handle both "2025-11-20T11:55" and ISO format with timezone
                if (deadlineStr.contains("T") && !deadlineStr.contains("Z") && !deadlineStr.contains("+")) {
                    // datetime-local format: "2025-11-20T11:55"
                    deadlineInstant = Instant.parse(deadlineStr + ":00Z");
                } else {
                    // ISO format with timezone
                    deadlineInstant = Instant.parse(deadlineStr);
                }

                Instant now = Instant.now();

                if (now.isAfter(deadlineInstant)) {
                    throw new RuntimeException("Form deadline expired");
                }
            } catch (Exception e) {
                System.err.println("Error parsing deadline: " + form.getFormsettings().getDeadline());
                e.printStackTrace();
                throw new RuntimeException("Invalid deadline format: " + e.getMessage());
            }
        }

        // 3️⃣ Check for multiple responses
        if (form.getFormsettings() != null && !form.getFormsettings().isAllowedMultipleResponses()) {

            String email = null;

            // Extract email from answers array
            if (response.getAnswers() instanceof List<?>) {
                List<Map<String, Object>> answersList = (List<Map<String, Object>>) response.getAnswers();
                for (Map<String, Object> ans : answersList) {
                    Object question = ans.get("question");
                    if (question != null && "email".equalsIgnoreCase(question.toString())) {
                        email = (String) ans.get("answer");
                        break;
                    }
                }
            }

            // If email found → check duplicate submission
            if (email != null && Boolean.TRUE.equals(
                    responseRepository.existsByEmail(response.getFormId(), email)
            )) {
                throw new RuntimeException("User already submitted response");
            }
        }


        response.setSubmittedAt(System.currentTimeMillis());
        return responseRepository.save(response);
    }

    public List<Form> getAllForms() {
        return formRepository.findAll();
    }


    public List<FormResponse> getResponses(String formId) {
        return responseRepository.findByFormId(formId);
    }

    public Form getForm(String formId) {
        return formRepository.findById(formId)
                .orElseThrow(() -> new NoSuchElementException("Form not found with id: " + formId));
    }

}

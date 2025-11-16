package org.example.service;

import org.example.entity.Form;
import org.example.repository.FormRepository;
import org.example.constants.constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormService {
    @Autowired
    private FormRepository formRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public Form createForm(Form form) {
        return formRepository.save(form);
    }
    public Form updateForm(Form form) {
        return formRepository.save(form);
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
}

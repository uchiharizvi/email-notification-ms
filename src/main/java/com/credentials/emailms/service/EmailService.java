package com.credentials.emailms.service;

import com.credentials.emailms.model.EmailTemplate;
import com.credentials.emailms.model.TemplateRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface EmailService {
    ResponseEntity<String> sendEmail(String recipient) throws IOException;

    void createEmailTemplate(TemplateRequest templateRequest);

    ResponseEntity<EmailTemplate> retrieveEmailTemplate(String templateName);
}

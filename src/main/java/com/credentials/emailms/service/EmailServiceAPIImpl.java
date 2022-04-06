package com.credentials.emailms.service;

import com.credentials.emailms.model.EmailTemplate;
import com.credentials.emailms.model.TemplateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service("EmailApi")
public class EmailServiceAPIImpl implements EmailService{
    @Override
    public ResponseEntity<String> sendEmail(String recipient) throws IOException {
        return null;
    }

    @Override
    public void createEmailTemplate(TemplateRequest templateRequest) {

    }

    @Override
    public ResponseEntity<EmailTemplate> retrieveEmailTemplate(String templateName) {
        return null;
    }
}

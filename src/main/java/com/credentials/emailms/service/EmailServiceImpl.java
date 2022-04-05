package com.credentials.emailms.service;

import com.credentials.emailms.model.EmailMessage;
import com.credentials.emailms.model.EmailTemplate;
import com.credentials.emailms.model.TemplateRequest;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    RestTemplate template;

    @Override
    public ResponseEntity<String> sendEmail(String recipient) {
        EmailTemplate emailTemplate = retrieveEmailTemplate("").getBody();
        retrieveDataFromSources();

        /*SES send email API Call*/
        ResponseEntity<String> emailStatus = template.postForEntity("/v2/email/outbound-emails", buildEmailMessage(), String.class);
        return null;
    }

    /*Admin call*/
    @Override
    public void createEmailTemplate(TemplateRequest templateRequest) {
        String response = template.postForObject("/v2/email/templates",getRequestEntity(templateRequest), String.class );
    }

    @Override
    public ResponseEntity<EmailTemplate> retrieveEmailTemplate(String templateName) {
       EmailTemplate emailTemplate = template.getForObject("/v2/email/templates/" + templateName, EmailTemplate.class);
        return new ResponseEntity<EmailTemplate>(emailTemplate, HttpStatus.OK);
    }

    private HttpEntity<?> getRequestEntity(TemplateRequest templateRequest) {
        Headers headers = new Headers();
        return new HttpEntity<TemplateRequest>(templateRequest);

    }

    private EmailMessage buildEmailMessage() {
        return new EmailMessage();
    }

    private void retrieveDataFromSources() {
    }

}

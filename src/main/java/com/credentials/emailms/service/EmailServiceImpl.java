package com.credentials.emailms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    RestTemplate template;

    @Override
    public ResponseEntity<String> sendEmail(String recipient) {
        retrieveEmailTemplate();
        retrieveDataFromSources();
        buildEmailMessage();

        return null;
    }

    private void buildEmailMessage() {
    }

    private void retrieveDataFromSources() {
    }

    private void retrieveEmailTemplate() {
    }
}

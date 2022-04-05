package com.credentials.emailms.service;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<String> sendEmail(String recipient);
}

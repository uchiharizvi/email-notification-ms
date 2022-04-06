package com.credentials.emailms.controller;

import com.credentials.emailms.service.EmailService;
import com.credentials.emailms.service.EmailServiceAPIImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Qualifier("EmailApi")
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam("abc") String recipient) throws IOException {
        return emailService.sendEmail(recipient);
    }
}

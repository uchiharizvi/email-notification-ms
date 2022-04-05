package com.credentials.emailms.controller;

import com.credentials.emailms.model.TemplateRequest;
import com.credentials.emailms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/email")
public class AdminEmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/template/create")
    public ResponseEntity<String> createTemplate(@RequestBody TemplateRequest templateRequest) {
         emailService.createEmailTemplate(templateRequest);
        return null;
    }

    @GetMapping("/template/{templateName}")
    public ResponseEntity<String> retrieveTemplate(@PathVariable String templateName) {
        emailService.retrieveEmailTemplate(templateName);
        return null;
    }
}

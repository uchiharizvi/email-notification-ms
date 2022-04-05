package com.credentials.emailms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.javafx.beans.IDProperty;
import lombok.Data;

@Data
public class TemplateContent {
    @JsonProperty("Html")
    private String html;

    @JsonProperty("Subject")
    private String subject;

    @JsonProperty("Text")
    private String text;
}

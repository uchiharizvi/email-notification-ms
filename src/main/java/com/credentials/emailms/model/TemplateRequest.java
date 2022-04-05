package com.credentials.emailms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TemplateRequest {
    @JsonProperty("TemplateContent")
    private TemplateContent templateContent;

    @JsonProperty("TemplateName")
    private String templateName;
}

package com.foodzy.web.az.freemarker.service;

import com.foodzy.web.az.freemarker.domain.FreemarkerTemplateType;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class FreemarkerTemplateService {

    public Template getTemplate(FreemarkerTemplateType type, String templateName) throws IllegalArgumentException, IOException {
        if (Objects.isNull(type))
            throw new IllegalArgumentException("Failed to get Template (reason: invalid Freemarker Template type).");
        if (StringUtils.isBlank(templateName))
            throw new IllegalArgumentException("Failed to get Template (reason: invalid Freemarker Template name).");
        return type.getConfiguration().getTemplate(templateName);
    }
}

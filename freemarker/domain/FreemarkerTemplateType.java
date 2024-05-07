package com.foodzy.web.az.freemarker.domain;

import freemarker.template.Configuration;
import lombok.Getter;

/**
 * @author Ngoc
 * @since 1.0.0
 */
public enum FreemarkerTemplateType {
    ACCOUNT("/freemarker_template/account/"),
    ACCOUNT_EVENT("/freemarker_template/account/marketing_event/"),
    CAMPAIGN_EVENT("/freemarker_template/campaign/event/"),
    CAMPAIGN_REPORT("/freemarker_template/campaign/report/"),
    UTILITY("/freemarker_template/utility/");

    @Getter
    private String basePackagePath;

    @Getter
    private Configuration configuration;

    FreemarkerTemplateType(String basePackagePath) {
        this.basePackagePath = basePackagePath;
        this.configuration = new Configuration(Configuration.VERSION_2_3_27);
        this.configuration.setClassForTemplateLoading(this.getClass(), basePackagePath);
        this.configuration.setDefaultEncoding("UTF-8");
    }
}

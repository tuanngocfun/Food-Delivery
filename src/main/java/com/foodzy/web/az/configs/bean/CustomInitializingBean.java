package com.foodzy.web.az.configs.bean;

import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@Component
public class CustomInitializingBean implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(CustomInitializingBean.class);

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    public ModelMapper modelMapper() {
        logger.info("Initializing ModelMapper...");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public EmailValidator emailValidator() {
        logger.info("Initializing EmailValidator...");
        return EmailValidator.getInstance();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        logger.info("Initializing MultipartConfigElement...");
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(5, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(5, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

//    @Bean
//    public Scheduler scheduler() throws IOException, SchedulerException {
//        logger.info("Initializing Quartz scheduler...");
//        String propertiesFileName = String.format("quartz.%s.properties", this.activeProfile);
//        InputStream quartzPropertiesFileInputStream = new ClassPathResource(propertiesFileName).getInputStream();
//        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
//        stdSchedulerFactory.initialize(quartzPropertiesFileInputStream);
//        Scheduler scheduler = stdSchedulerFactory.getScheduler();
//        scheduler.start();
//        return scheduler;
//    }

    @Bean
    public CookieSerializer cookieSerializer() {
        logger.info("Initializing CookieSerializer...");
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("none");
        serializer.setUseSecureCookie(true);
        return serializer;
    }
}

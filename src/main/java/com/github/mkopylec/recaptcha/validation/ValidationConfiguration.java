package com.github.mkopylec.recaptcha.validation;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("recaptchaValidationConfiguration")
@EnableConfigurationProperties(RecaptchaProperties.class)
@ConditionalOnProperty(name = "recaptcha.testing.enabled", havingValue = "false", matchIfMissing = true)
public class ValidationConfiguration {

    @Autowired
    private RecaptchaProperties recaptcha;

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaValidator userResponseValidator(RestTemplate restTemplate) {
        return new RecaptchaValidator(restTemplate, recaptcha);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

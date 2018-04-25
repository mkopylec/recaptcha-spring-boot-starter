package com.github.mkopylec.recaptcha.validation;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
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

    private final RecaptchaProperties recaptcha;

    public ValidationConfiguration(RecaptchaProperties recaptcha) {
        this.recaptcha = recaptcha;
    }

    // TODO timeouts
    @Bean
    @ConditionalOnMissingBean
    public RecaptchaValidator userResponseValidator() {
        return new RecaptchaValidator(new RestTemplate(), recaptcha);
    }
}

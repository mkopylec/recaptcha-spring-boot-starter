package com.github.mkopylec.recaptcha.webflux.testing;

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties;
import com.github.mkopylec.recaptcha.webflux.validation.RecaptchaValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RecaptchaProperties.class)
@ConditionalOnProperty(name = "recaptcha.testing.enabled")
public class TestingConfiguration {

    private final RecaptchaProperties recaptcha;

    public TestingConfiguration(RecaptchaProperties recaptcha) {
        this.recaptcha = recaptcha;
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaValidator webFluxRecaptchaValidator() {
        return new TestRecaptchaValidator(recaptcha);
    }
}

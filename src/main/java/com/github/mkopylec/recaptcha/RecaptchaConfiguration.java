package com.github.mkopylec.recaptcha;

import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationFilter;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(RecaptchaProperties.class)
public class RecaptchaConfiguration {

    @Autowired
    private RecaptchaProperties recaptcha;

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaAuthenticationFilter recaptchaAuthenticationFilter(RecaptchaValidator recaptchaValidator) {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        for (String securedPath : recaptcha.getSecurity().getSecuredPaths()) {
            requestMatchers.add(new AntPathRequestMatcher(securedPath, "POST"));
        }
        return new RecaptchaAuthenticationFilter(new OrRequestMatcher(requestMatchers), recaptchaValidator, recaptcha.getSecurity());
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaValidator userResponseValidator(RestTemplate restTemplate) {
        return new RecaptchaValidator(restTemplate, recaptcha.getValidation());
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

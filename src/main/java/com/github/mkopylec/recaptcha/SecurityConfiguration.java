package com.github.mkopylec.recaptcha;

import com.github.mkopylec.recaptcha.security.RecaptchaAuthenticationFilter;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(EnableWebSecurity.class)
@EnableConfigurationProperties(RecaptchaProperties.class)
public class SecurityConfiguration {

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
}

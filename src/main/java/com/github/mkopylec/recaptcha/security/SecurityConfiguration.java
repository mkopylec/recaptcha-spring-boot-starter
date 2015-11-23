package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;

@Configuration("recaptchaSecurityConfiguration")
@ConditionalOnClass({EnableWebSecurity.class, AbstractAuthenticationProcessingFilter.class})
@EnableConfigurationProperties(RecaptchaProperties.class)
public class SecurityConfiguration {

    @Autowired
    private RecaptchaProperties recaptcha;
    @Lazy
    @Autowired
    private HttpServletRequest request;

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaAuthenticationFilter recaptchaAuthenticationFilter(RecaptchaValidator recaptchaValidator, LoginFailuresManager failuresManager) {
        return new RecaptchaAuthenticationFilter(recaptchaValidator, recaptcha, failuresManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresManager loginFailuresManager() {
        return new SessionLoginFailuresManager(request);
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaLoginEntryPoint recaptchaLoginEntryPoint(LoginFailuresManager failuresManager) {
        return new RecaptchaLoginEntryPoint(failuresManager, recaptcha.getSecurity());
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresCountingHandler loginFailuresCountingHandler(LoginFailuresManager failuresManager) {
        return new LoginFailuresCountingHandler(failuresManager);
    }
}

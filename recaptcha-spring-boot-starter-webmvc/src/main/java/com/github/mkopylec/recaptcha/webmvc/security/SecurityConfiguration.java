package com.github.mkopylec.recaptcha.webmvc.security;

import com.github.mkopylec.recaptcha.commons.RecaptchaProperties;
import com.github.mkopylec.recaptcha.webmvc.security.login.FormLoginConfigurerEnhancer;
import com.github.mkopylec.recaptcha.webmvc.security.login.InMemoryLoginFailuresManager;
import com.github.mkopylec.recaptcha.webmvc.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.webmvc.security.login.LoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.webmvc.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.webmvc.security.login.RecaptchaAwareRedirectStrategy;
import com.github.mkopylec.recaptcha.webmvc.validation.RecaptchaValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Configuration
@ConditionalOnClass({EnableWebSecurity.class, AbstractAuthenticationProcessingFilter.class})
public class SecurityConfiguration {

    private final RecaptchaProperties recaptcha;

    public SecurityConfiguration(RecaptchaProperties recaptcha) {
        this.recaptcha = recaptcha;
    }

    @Bean
    @ConditionalOnMissingBean
    public FormLoginConfigurerEnhancer webMvcFormLoginConfigurerEnhancer(LoginFailuresClearingHandler successHandler, LoginFailuresCountingHandler failureHandler, RecaptchaValidator recaptchaValidator, LoginFailuresManager failuresManager) {
        RecaptchaAuthenticationFilter authenticationFilter = new RecaptchaAuthenticationFilter(recaptchaValidator, recaptcha, failuresManager);
        return new FormLoginConfigurerEnhancer(authenticationFilter, successHandler, failureHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresManager webMvcLoginFailuresManager() {
        return new InMemoryLoginFailuresManager(recaptcha);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresCountingHandler webMvcLoginFailuresCountingHandler(LoginFailuresManager failuresManager, RecaptchaAwareRedirectStrategy redirectStrategy) {
        return new LoginFailuresCountingHandler(failuresManager, recaptcha, redirectStrategy);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresClearingHandler webMvcLoginFailuresClearingHandler(LoginFailuresManager failuresManager) {
        return new LoginFailuresClearingHandler(failuresManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaAwareRedirectStrategy webMvcRecaptchaAwareRedirectStrategy(LoginFailuresManager failuresManager) {
        return new RecaptchaAwareRedirectStrategy(failuresManager);
    }
}

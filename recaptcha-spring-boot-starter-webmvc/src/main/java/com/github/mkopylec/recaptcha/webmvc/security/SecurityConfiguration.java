package com.github.mkopylec.recaptcha.webmvc.security;

import com.github.mkopylec.recaptcha.webmvc.RecaptchaProperties;
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

@Configuration("recaptchaSecurityConfiguration")
@ConditionalOnClass({EnableWebSecurity.class, AbstractAuthenticationProcessingFilter.class})
public class SecurityConfiguration {

    private final RecaptchaProperties recaptcha;

    public SecurityConfiguration(RecaptchaProperties recaptcha) {
        this.recaptcha = recaptcha;
    }

    @Bean
    @ConditionalOnMissingBean
    public FormLoginConfigurerEnhancer formLoginConfigurerEnhancer(LoginFailuresClearingHandler successHandler, LoginFailuresCountingHandler failureHandler, RecaptchaValidator recaptchaValidator, LoginFailuresManager failuresManager, AuthenticationRequestParser requestParser) {
        RecaptchaAuthenticationFilter authenticationFilter = new RecaptchaAuthenticationFilter(recaptchaValidator, recaptcha, failuresManager, requestParser);
        return new FormLoginConfigurerEnhancer(authenticationFilter, successHandler, failureHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresManager loginFailuresManager() {
        return new InMemoryLoginFailuresManager(recaptcha);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresCountingHandler loginFailuresCountingHandler(LoginFailuresManager failuresManager, RecaptchaAwareRedirectStrategy redirectStrategy) {
        return new LoginFailuresCountingHandler(failuresManager, recaptcha, redirectStrategy);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresClearingHandler loginFailuresClearingHandler(LoginFailuresManager failuresManager) {
        return new LoginFailuresClearingHandler(failuresManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaAwareRedirectStrategy recaptchaAwareRedirectStrategy(LoginFailuresManager failuresManager) {
        return new RecaptchaAwareRedirectStrategy(failuresManager);
    }
}

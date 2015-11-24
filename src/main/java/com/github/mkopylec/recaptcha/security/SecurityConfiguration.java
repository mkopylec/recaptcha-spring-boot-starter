package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.login.CredentialLoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.security.login.InMemoryLoginFailuresManager;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.security.login.RecaptchaLoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Configuration("recaptchaSecurityConfiguration")
@ConditionalOnClass({EnableWebSecurity.class, AbstractAuthenticationProcessingFilter.class})
@EnableConfigurationProperties(RecaptchaProperties.class)
public class SecurityConfiguration {

    @Autowired
    private RecaptchaProperties recaptcha;

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaAuthenticationFilter recaptchaAuthenticationFilter(
            RecaptchaValidator recaptchaValidator, LoginFailuresManager failuresManager, RecaptchaLoginFailuresCountingHandler failureHandler
    ) {
        return new RecaptchaAuthenticationFilter(recaptchaValidator, recaptcha, failuresManager, failureHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresManager loginFailuresManager() {
        return new InMemoryLoginFailuresManager(recaptcha.getSecurity());
    }

    @Bean
    @ConditionalOnMissingBean
    public CredentialLoginFailuresCountingHandler credentialLoginFailuresCountingHandler(LoginFailuresManager failuresManager) {
        return new CredentialLoginFailuresCountingHandler(failuresManager, recaptcha.getSecurity());
    }

    @Bean
    @ConditionalOnMissingBean
    public RecaptchaLoginFailuresCountingHandler recaptchaFailuresCountingHandler(LoginFailuresManager failuresManager) {
        return new RecaptchaLoginFailuresCountingHandler(failuresManager, recaptcha.getSecurity());
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginFailuresClearingHandler loginFailuresClearingHandler(LoginFailuresManager failuresManager) {
        return new LoginFailuresClearingHandler(failuresManager, recaptcha.getSecurity());
    }
}

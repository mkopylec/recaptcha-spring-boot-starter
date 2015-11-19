package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.Assert.notNull;

public class RecaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    protected RecaptchaValidator recaptchaValidator;

    public RecaptchaAuthenticationFilter(RequestMatcher requestMatcher, RecaptchaValidator recaptchaValidator, Security recaptcha) {
        super(requestMatcher);
        this.recaptchaValidator = recaptchaValidator;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(recaptcha.getFailureUrl()));
        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ValidationResult result = recaptchaValidator.validate(request);
        if (result.isSuccess()) {
            return new PreAuthenticatedAuthenticationToken("reCAPTCHA", null);
        }
        throw new RecaptchaAuthenticationException(result.getErrorCodes());
    }

    @Override
    public void afterPropertiesSet() {
        notNull(recaptchaValidator, "Missing recaptcha validator");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    }
}

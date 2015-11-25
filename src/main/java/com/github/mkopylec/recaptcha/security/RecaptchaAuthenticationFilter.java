package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_CAPTCHA_RESPONSE_PARAMETER;
import static com.github.mkopylec.recaptcha.validation.ErrorCode.MISSING_USERNAME_REQUEST_PARAMETER;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.notNull;

public class RecaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = getLogger(RecaptchaAuthenticationFilter.class);

    protected final RecaptchaValidator recaptchaValidator;
    protected final RecaptchaProperties recaptcha;
    protected final LoginFailuresManager failuresManager;

    public RecaptchaAuthenticationFilter(
            RecaptchaValidator recaptchaValidator,
            RecaptchaProperties recaptcha,
            LoginFailuresManager failuresManager,
            LoginFailuresCountingHandler failureHandler,
            LoginFailuresClearingHandler successHandler
    ) {
        this.recaptchaValidator = recaptchaValidator;
        this.recaptcha = recaptcha;
        this.failuresManager = failuresManager;
        setAuthenticationFailureHandler(failureHandler);
        setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (getUsernameParameter() == null) {
            throw new RecaptchaAuthenticationException(singletonList(MISSING_USERNAME_REQUEST_PARAMETER));
        }
        if (failuresManager.isRecaptchaRequired(getUsernameParameter())) {
            log.debug("reCAPTCHA required for username: {}", getUsernameParameter());
            if (hasNoRecaptchaResponse(request)) {
                throw new RecaptchaAuthenticationException(singletonList(MISSING_CAPTCHA_RESPONSE_PARAMETER));
            }
            ValidationResult result = recaptchaValidator.validate(request);
            if (result.isFailure()) {
                throw new RecaptchaAuthenticationException(result.getErrorCodes());
            }
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        if (LoginFailuresClearingHandler.class.isAssignableFrom(successHandler.getClass())) {
            LoginFailuresClearingHandler handler = (LoginFailuresClearingHandler) successHandler;
            handler.setUsernameParameter(getUsernameParameter());
            super.setAuthenticationSuccessHandler(handler);
        }
        throw new IllegalArgumentException("Invalid login success handler. " + successHandler + " must be instance of " + LoginFailuresClearingHandler.class.getName());
    }

    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        if (LoginFailuresCountingHandler.class.isAssignableFrom(failureHandler.getClass())) {
            LoginFailuresCountingHandler handler = (LoginFailuresCountingHandler) failureHandler;
            handler.setUsernameParameter(getUsernameParameter());
            super.setAuthenticationFailureHandler(handler);
        }
        throw new IllegalArgumentException("Invalid login failure handler. " + failureHandler + " must be instance of " + LoginFailuresCountingHandler.class.getName());
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        notNull(recaptchaValidator, "Missing recaptcha validator");
        notNull(recaptcha, "Missing recaptcha configuration properties");
        notNull(failuresManager, "Missing login failure manager");
    }

    protected boolean hasNoRecaptchaResponse(HttpServletRequest request) {
        return !request.getParameterMap().containsKey(recaptcha.getValidation().getResponseParameter());
    }
}

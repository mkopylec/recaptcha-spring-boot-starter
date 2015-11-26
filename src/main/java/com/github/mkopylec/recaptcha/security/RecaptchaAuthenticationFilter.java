package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Validation;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresCountingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
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
import static org.springframework.util.Assert.notNull;

public class RecaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    protected final RecaptchaValidator recaptchaValidator;
    protected final Validation validation;
    protected final LoginFailuresManager failuresManager;

    public RecaptchaAuthenticationFilter(
            RecaptchaValidator recaptchaValidator,
            RecaptchaProperties recaptcha,
            LoginFailuresManager failuresManager
    ) {
        this.recaptchaValidator = recaptchaValidator;
        validation = recaptcha.getValidation();
        this.failuresManager = failuresManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (getUsernameParameter() == null) {
            throw new RecaptchaAuthenticationException(singletonList(MISSING_USERNAME_REQUEST_PARAMETER));
        }
        if (failuresManager.isRecaptchaRequired(request)) {
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
        if (!LoginFailuresClearingHandler.class.isAssignableFrom(successHandler.getClass())) {
            throw new IllegalArgumentException("Invalid login success handler. Handler must be an instance of " + LoginFailuresClearingHandler.class.getName() + " but is " + successHandler);
        }
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        if (!LoginFailuresCountingHandler.class.isAssignableFrom(failureHandler.getClass())) {
            throw new IllegalArgumentException("Invalid login failure handler. Handler must be an instance of " + LoginFailuresCountingHandler.class.getName() + " but is " + failureHandler);
        }
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public void setUsernameParameter(String usernameParameter) {
        super.setUsernameParameter(usernameParameter);
        failuresManager.setUsernameParameter(usernameParameter);
    }

    @Override
    public void afterPropertiesSet() {
        notNull(recaptchaValidator, "Missing recaptcha validator");
        notNull(validation, "Missing recaptcha validation configuration properties");
        notNull(failuresManager, "Missing login failure manager");
    }

    protected boolean hasNoRecaptchaResponse(HttpServletRequest request) {
        return !request.getParameterMap().containsKey(validation.getResponseParameter());
    }
}

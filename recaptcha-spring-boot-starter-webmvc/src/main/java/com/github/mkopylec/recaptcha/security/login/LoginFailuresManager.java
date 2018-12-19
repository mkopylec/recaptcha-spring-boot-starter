package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class LoginFailuresManager {

    private static final Logger log = getLogger(LoginFailuresManager.class);

    protected final Security security;
    protected String usernameParameter;

    public LoginFailuresManager(RecaptchaProperties recaptcha) {
        security = recaptcha.getSecurity();
    }

    public abstract void addLoginFailure(HttpServletRequest request);

    public abstract int getLoginFailuresCount(HttpServletRequest request);

    public abstract void clearLoginFailures(HttpServletRequest request);

    public boolean isRecaptchaRequired(HttpServletRequest request) {
        boolean recaptchaRequired = getLoginFailuresCount(request) >= security.getLoginFailuresThreshold();
        log.debug("Done checking is reCAPTCHA required for username: {}. Check result: {}", getUsername(request), recaptchaRequired);
        return recaptchaRequired;
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    protected String getUsername(HttpServletRequest request) {
        if (usernameParameter == null) {
            throw new IllegalStateException("Missing username parameter name");
        }
        String username = request.getParameter(usernameParameter);
        if (username == null) {
            throw new IllegalStateException("Missing username parameter '" + usernameParameter + "' value in HTTP request");
        }
        return username;
    }
}

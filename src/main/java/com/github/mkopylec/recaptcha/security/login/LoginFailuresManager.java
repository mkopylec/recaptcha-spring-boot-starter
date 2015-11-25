package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;

import javax.servlet.http.HttpServletRequest;

public abstract class LoginFailuresManager {

    protected final Security security;
    protected String usernameParameter;

    public LoginFailuresManager(RecaptchaProperties recaptcha) {
        security = recaptcha.getSecurity();
    }

    public abstract void addLoginFailure(HttpServletRequest request);

    public abstract int getLoginFailuresCount(HttpServletRequest request);

    public abstract void clearLoginFailures(HttpServletRequest request);

    public boolean isRecaptchaRequired(HttpServletRequest request) {
        return getLoginFailuresCount(request) >= security.getLoginFailuresThreshold();
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

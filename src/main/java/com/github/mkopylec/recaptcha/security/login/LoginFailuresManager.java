package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;

public abstract class LoginFailuresManager {

    protected final Security security;

    public LoginFailuresManager(RecaptchaProperties recaptcha) {
        security = recaptcha.getSecurity();
    }

    public abstract void addLoginFailure(String username);

    public abstract int getLoginFailuresCount(String username);

    public abstract void resetLoginFailures(String username);

    public boolean isRecaptchaRequired(String username) {
        return getLoginFailuresCount(username) >= security.getLoginFailuresThreshold();
    }
}

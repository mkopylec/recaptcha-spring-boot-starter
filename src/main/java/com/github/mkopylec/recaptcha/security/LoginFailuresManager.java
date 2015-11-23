package com.github.mkopylec.recaptcha.security;

public interface LoginFailuresManager {

    void addLoginFailure();

    int getLoginFailuresCount();

    void resetLoginFailures();
}

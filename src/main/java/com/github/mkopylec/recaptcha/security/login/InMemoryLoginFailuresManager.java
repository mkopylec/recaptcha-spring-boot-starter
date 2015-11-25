package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryLoginFailuresManager extends LoginFailuresManager {

    protected final ConcurrentMap<String, Integer> loginFailures = new ConcurrentHashMap<>();

    public InMemoryLoginFailuresManager(RecaptchaProperties recaptcha) {
        super(recaptcha);
    }

    @Override
    public void addLoginFailure(String username) {
        loginFailures.put(username, getLoginFailuresCount(username) + 1);
    }

    @Override
    public int getLoginFailuresCount(String username) {
        return loginFailures.get(username) == null ? 0 : loginFailures.get(username);
    }

    @Override
    public void resetLoginFailures(String username) {
        loginFailures.remove(username);
    }
}

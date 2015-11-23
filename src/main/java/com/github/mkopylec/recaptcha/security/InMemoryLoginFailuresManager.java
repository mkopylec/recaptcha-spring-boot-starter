package com.github.mkopylec.recaptcha.security;

import com.github.mkopylec.recaptcha.RecaptchaProperties.Security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryLoginFailuresManager extends LoginFailuresManager {

    protected final ConcurrentMap<String, Integer> loginFailures = new ConcurrentHashMap<>();

    public InMemoryLoginFailuresManager(Security security) {
        super(security);
    }

    @Override
    public void addLoginFailure(String username) {
        int loginFailuresCount = loginFailures.get(username) == null ? 1 : loginFailures.get(username);
        loginFailures.put(username, ++loginFailuresCount);
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

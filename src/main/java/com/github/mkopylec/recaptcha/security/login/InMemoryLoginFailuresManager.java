package com.github.mkopylec.recaptcha.security.login;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryLoginFailuresManager extends LoginFailuresManager {

    private static final Logger log = getLogger(InMemoryLoginFailuresManager.class);

    protected final ConcurrentMap<String, Integer> loginFailures = new ConcurrentHashMap<>();

    public InMemoryLoginFailuresManager(RecaptchaProperties recaptcha) {
        super(recaptcha);
    }

    @Override
    public void addLoginFailure(HttpServletRequest request) {
        String username = getUsername(request);
        log.debug("Adding login failure for username: {}", username);
        loginFailures.compute(username, (name, count) -> count == null ? 1 : count + 1);
    }

    @Override
    public int getLoginFailuresCount(HttpServletRequest request) {
        String username = getUsername(request);
        int count = loginFailures.getOrDefault(username, 0);
        log.debug("Getting login failures count: {} for username: {}", count, username);
        return count;
    }

    @Override
    public void clearLoginFailures(HttpServletRequest request) {
        String username = getUsername(request);
        log.debug("Clearing login failures for username: {}", username);
        loginFailures.remove(username);
    }
}

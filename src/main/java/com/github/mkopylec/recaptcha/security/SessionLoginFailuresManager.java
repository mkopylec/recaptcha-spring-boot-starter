package com.github.mkopylec.recaptcha.security;

import javax.servlet.http.HttpServletRequest;

public class SessionLoginFailuresManager implements LoginFailuresManager {

    public static final String LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE = "__login_failures_count__";

    protected final HttpServletRequest request;

    public SessionLoginFailuresManager(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void addLoginFailure() {
        Object loginFailuresCount = request.getSession().getAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE);
        int newFailuresCount = 1;
        if (loginFailuresCount != null) {
            newFailuresCount = ((int) loginFailuresCount) + 1;
        }
        request.getSession().setAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE, newFailuresCount);
    }

    @Override
    public int getLoginFailuresCount() {
        return (int) request.getSession().getAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE);
    }

    @Override
    public void resetLoginFailures() {
        request.getSession().removeAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE);
    }
}
